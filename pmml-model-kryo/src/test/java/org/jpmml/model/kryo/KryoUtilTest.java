/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import org.dmg.pmml.False;
import org.dmg.pmml.InlineTableTest;
import org.dmg.pmml.PMML;
import org.dmg.pmml.True;
import org.dmg.pmml.tree.BranchNode;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.jpmml.model.MixedContentTest;
import org.jpmml.model.ResourceUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class KryoUtilTest {

	private Kryo kryo = null;


	@Before
	public void setUp(){
		Kryo kryo = new Kryo();

		KryoUtil.init(kryo);
		KryoUtil.register(kryo);

		this.kryo = kryo;
	}

	@Test
	public void referenceTest(){
		Node leftChild = new LeafNode()
			.setPredicate(True.INSTANCE);

		Node rightChild = new LeafNode()
			.setPredicate(False.INSTANCE);

		Node parent = new BranchNode()
			.setPredicate(True.INSTANCE)
			.setDefaultChild(leftChild)
			.addNodes(leftChild, rightChild);

		Node clonedParent = KryoUtil.clone(this.kryo, parent);

		List<Node> clonedChildren = clonedParent.getNodes();

		assertNotSame(parent, clonedParent);

		Node clonedLeftChild = clonedChildren.get(0);
		Node clonedRightChild = clonedChildren.get(1);

		assertSame(clonedParent.getDefaultChild(), clonedLeftChild);

		assertNotSame(leftChild, clonedLeftChild);
		assertNotSame(rightChild, clonedRightChild);

		assertNotSame(parent.getPredicate(), clonedParent.getPredicate());
		assertSame(clonedParent.getPredicate(), clonedLeftChild.getPredicate());
	}

	@Test
	public void inlineTableTest() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(InlineTableTest.class);

		KryoUtil.clone(this.kryo, pmml);
	}

	@Test
	public void mixedContent() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(MixedContentTest.class);

		KryoUtil.clone(this.kryo, pmml);
	}
}