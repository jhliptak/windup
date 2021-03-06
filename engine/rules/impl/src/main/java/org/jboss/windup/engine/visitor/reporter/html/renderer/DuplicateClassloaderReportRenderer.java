package org.jboss.windup.engine.visitor.reporter.html.renderer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.jboss.windup.engine.visitor.AbstractGraphVisitor;
import org.jboss.windup.engine.visitor.VisitorPhase;
import org.jboss.windup.engine.visitor.reporter.html.model.ApplicationContext;
import org.jboss.windup.engine.visitor.reporter.html.model.ClassloaderReport;
import org.jboss.windup.engine.visitor.reporter.html.model.SimpleName;
import org.jboss.windup.engine.visitor.reporter.html.model.ClassloaderReport.ClassLoaderReportRow;
import org.jboss.windup.engine.visitor.reporter.html.model.ClassloaderReport.ClassReference;
import org.jboss.windup.engine.visitor.reporter.html.model.Name;
import org.jboss.windup.graph.WindupContext;
import org.jboss.windup.graph.dao.JavaClassDao;
import org.jboss.windup.graph.model.resource.ArchiveResource;
import org.jboss.windup.graph.model.resource.JavaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class DuplicateClassloaderReportRenderer extends AbstractGraphVisitor
{
    private static final Logger LOG = LoggerFactory.getLogger(DuplicateClassloaderReportRenderer.class);

    @Inject
    private WindupContext context;

    @Inject
    private JavaClassDao javaClassDao;

    @Inject
    private NamingUtility namingUtility;

    private Configuration cfg;
    private File runDirectory;
    private File reportReference;

    @Override
    public VisitorPhase getPhase()
    {
        return VisitorPhase.REPORTING;
    }
    
    @PostConstruct
    private void postConstruct() throws IOException
    {
        cfg = new Configuration();
        cfg.setTemplateUpdateDelay(500);
        cfg.setClassForTemplateLoading(this.getClass(), "/");

        runDirectory = context.getRunDirectory();
        File archiveReportDirectory = new File(runDirectory, "applications");
        File archiveDirectory = new File(archiveReportDirectory, "application");
        FileUtils.forceMkdir(archiveDirectory);
        reportReference = new File(archiveDirectory, "classloader-duplicate.html");
    }

    @Override
    public void run()
    {
        try
        {
            Template template = cfg.getTemplate("/reports/templates/classloader.ftl");

            ApplicationContext appCtx = new ApplicationContext(namingUtility.getApplicationName());

            ClassloaderReport report = new ClassloaderReport("Duplicates", "Duplicate", "Provided By");

            // for each class leveraging a blacklist...
            for (JavaClass clz : javaClassDao.getAllDuplicateClasses())
            {
                Name name = namingUtility.getReportJavaResource(runDirectory, reportReference.getParentFile(), clz);

                // get reference...
                ClassLoaderReportRow row = new ClassLoaderReportRow(name);
                addAll(row.getReferences(), clz.getArchivesProvidingClass());

                report.getClasses().add(row);
            }

            Map<String, Object> objects = new HashMap<String, Object>();
            objects.put("application", appCtx);
            objects.put("classloader", report);

            template.process(objects, new FileWriter(reportReference));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Exception processing report.", e);
        }

    }

    public void addAll(Collection<ClassReference> references, Iterable<ArchiveResource> archives)
    {
        for (ArchiveResource resource : archives)
        {
            // build the full path...
            Name name = new SimpleName(namingUtility.buildFullPath(resource));
            ClassReference clzRef = new ClassReference("", name);
            references.add(clzRef);
        }
    }
}
