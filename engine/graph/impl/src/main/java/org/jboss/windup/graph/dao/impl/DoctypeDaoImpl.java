package org.jboss.windup.graph.dao.impl;

import java.util.Iterator;

import javax.inject.Singleton;

import org.apache.commons.lang.StringUtils;
import org.jboss.windup.graph.dao.DoctypeDao;
import org.jboss.windup.graph.model.meta.xml.DoctypeMeta;

import com.google.common.collect.Iterables;
import com.tinkerpop.frames.FramedGraphQuery;
import com.tinkerpop.gremlin.java.GremlinPipeline;

@Singleton
public class DoctypeDaoImpl extends BaseDaoImpl<DoctypeMeta> implements DoctypeDao {

	public DoctypeDaoImpl() {
		super(DoctypeMeta.class);
	}

	public Iterable<DoctypeMeta> findSystemIdOrPublicIdMatchingRegex(String ... regex) {
		//iterate through all vertices
 		Iterable<DoctypeMeta> results = Iterables.concat(findSystemIdMatchingRegex(regex), findPublicIdMatchingRegex(regex));
		GremlinPipeline<DoctypeMeta, DoctypeMeta> pipe = new GremlinPipeline<DoctypeMeta, DoctypeMeta>(results).dedup();
		return pipe;
	}
	
	
	public Iterable<DoctypeMeta> findSystemIdMatchingRegex(String ... regex) {
		return super.findValueMatchingRegex("systemId", regex);
	}
	
	public Iterable<DoctypeMeta> findPublicIdMatchingRegex(String ... regex) {
		return super.findValueMatchingRegex("publicId", regex);
	}
	

	public Iterator<DoctypeMeta> findByProperties(String publicId, String systemId) {
		FramedGraphQuery query = context.getFramed().query();
		if(StringUtils.isNotBlank(publicId)) {
			query.has("publicId", publicId);
		}
		if(StringUtils.isNotBlank(systemId)) {
			query.has("systemId", systemId);
		}
		return query.vertices(DoctypeMeta.class).iterator();
	}

}
