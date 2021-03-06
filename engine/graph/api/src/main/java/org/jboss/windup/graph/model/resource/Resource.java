package org.jboss.windup.graph.model.resource;

import java.io.File;
import java.io.InputStream;

import org.jboss.windup.graph.model.meta.Meta;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeField("type")
@TypeValue("BaseResource")
public interface Resource extends VertexFrame
{

    @Adjacency(label = "meta", direction = Direction.OUT)
    public Iterable<Meta> getMeta();

    @Adjacency(label = "meta", direction = Direction.OUT)
    public void addMeta(final Meta resource);

    public InputStream asInputStream();

    public File asFile() throws RuntimeException;
}
