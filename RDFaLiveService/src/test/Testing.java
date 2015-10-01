package test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLLink;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLNetwork;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class Testing {
	
	public void showGraph(RLNetwork net){
		//SimpleGraphView2 sgv = new SimpleGraphView2(); // This builds the graph
		 // Layout<V, E>, BasicVisualizationServer<V,E>
		 Layout<RLAtom, RLLink> layout = new CircleLayout<RLAtom, RLLink>(net.getNetGraph());
		 layout.setSize(new Dimension(1000,730));
		 BasicVisualizationServer<RLAtom, RLLink> vv = 
		 new BasicVisualizationServer<RLAtom, RLLink>(layout);
		 vv.setPreferredSize(new Dimension(1000,730)); 
		 // Setup up a new vertex to paint transformer...
		 Transformer<RLAtom,Paint> vertexPaint = new Transformer<RLAtom,Paint>() {
		 public Paint transform(RLAtom i) {
		 return Color.GREEN;
		 }
		 }; 
		 // Set up a new stroke Transformer for the edges
		 float dash[] = {10.0f};
		 final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
		 BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		 Transformer<RLLink, Stroke> edgeStrokeTransformer = 
		 new Transformer<RLLink, Stroke>() {
		 public Stroke transform(RLLink s) {
		 return edgeStroke;
		 }
		 };
		 vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		 vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		 vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<RLAtom>());
		 vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<RLLink>());
		 vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		 JFrame frame = new JFrame("Simple Graph View 2");
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.getContentPane().add(vv);
		 frame.pack();
		 frame.setVisible(true); 
	}
	
	public static void main(String[] args) {
		// Graph<V, E> where V is the type of the vertices 
		 // and E is the type of the edges
		 Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
		 // Add some vertices. From above we defined these to be type Integer.
		 g.addVertex((Integer)1);
		 g.addVertex((Integer)2);
		 g.addVertex((Integer)3); 
		 // Add some edges. From above we defined these to be of type String
		 // Note that the default is for undirected edges.
		 g.addEdge("Edge-A", 1, 2); // Note that Java 1.5 auto-boxes primitives
		 g.addEdge("Edge-B", 2, 3); 
		 // Let's see what we have. Note the nice output from the
		 // SparseMultigraph<V,E> toString() method
		 System.out.println("The graph g = " + g.toString());
		 // Note that we can use the same nodes and edges in two different graphs.
		 Graph<Integer, String> g2 = new SparseMultigraph<Integer, String>();
		 g2.addVertex((Integer)1);
		 g2.addVertex((Integer)2);
		 g2.addVertex((Integer)3); 
		 g2.addEdge("Edge-A", 1,3);
		 g2.addEdge("Edge-B", 2,3, EdgeType.DIRECTED);
		 g2.addEdge("Edge-C", 3, 2, EdgeType.DIRECTED);
		 g2.addEdge("Edge-P", 2,3); // A parallel edge
		 System.out.println("The graph g2 = " + g2.toString()); 
	}

}
