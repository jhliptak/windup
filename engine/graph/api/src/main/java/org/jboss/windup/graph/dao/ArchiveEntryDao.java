package org.jboss.windup.graph.dao;

import org.jboss.windup.graph.model.resource.ArchiveEntryResource;
import org.jboss.windup.graph.model.resource.ArchiveResource;

public interface ArchiveEntryDao extends BaseDao<ArchiveEntryResource>
{

    public Iterable<ArchiveEntryResource> findArchiveEntry(String value);

    public long findArchiveEntryWithExtensionCount(String... values);

    public Iterable<ArchiveEntryResource> findArchiveEntryWithExtension(String... values);

    public Iterable<ArchiveEntryResource> findByArchive(final ArchiveResource resource);
}
