package Solution;

import javax.swing.*;
import javax.swing.Box.*;
import java.awt.*;

import java.util.*;

/**
* A class to generate the GUI to display the Hierarchy, and allows selecting/diselecting the Hierarchy's
*
*
*@author X. Huang
*@version 1.0
*@since 2018-11-10
*/

public class Solution<T> {

	Map<String, JCheckBox> mMap = new HashMap<>();
	Hierarchy<T> mRoot;

	public Solution(Hierarchy<T> root){
		mRoot = root;
	}

	//Create the main GUI
	private void createAndShowGUI() {
		JFrame frame = new JFrame("Gross Chest Anatomy");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setPreferredSize(new Dimension(200, 400));

		addHierarchyToContainer(mRoot, frame.getContentPane(), 0);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2 - frame.getSize().width/2, dim.height/2 - frame.getSize().height/2);

		frame.pack();
		frame.setVisible(true);
	}

	//Recursively add the GUI for the Hierarchy and its children to the parent GUI container
	private void addHierarchyToContainer(Hierarchy<T> hierarchy, Container container, int level) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JCheckBox checkBox = new JCheckBox(hierarchy.data().toString()); //set the checkbox label here
		final String id = hierarchy.id();
		checkBox.addActionListener(
			event -> {
				JCheckBox source = (JCheckBox) event.getSource();
				onItemCheck(id, source.isSelected());
			});
		mMap.put(hierarchy.id(), checkBox);

		panel.add(Box.createRigidArea(new Dimension(level*20, 0))); //set the checkbox indentation here
		panel.add(checkBox);
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		container.add(panel);

		if (hierarchy.children() != null) {
			for (Hierarchy<T> child : hierarchy.children())
				addHierarchyToContainer(child, panel, level+1);
		}
	}

	private void onItemCheck(String id, boolean checked){
		if (checked)
			checkItem(id);
		else
			uncheckItem(id);
	}

	//Recursively check an item and its ancestors
	private void checkItem(String id) {
		mMap.get(id).setSelected(true);
		Hierarchy<T> parent = mRoot.getById(id).parent();
		if (parent != null)
			checkItem(parent.id());
	}

	//Recursively uncheck and item and its children
	private void uncheckItem(String id) {
		mMap.get(id).setSelected(false);
		Collection<Hierarchy<T>> children = mRoot.getById(id).children();
		if (children != null)
			for (Hierarchy<T> child : children)
				uncheckItem(child.id());
	}

	//Create a String Hierarchy and display
	public static void main(String[] args) {

		//Create the Hierachy
		Hierarchy<String> chest = new Hierarchy<>("Chest");
		chest.get("Chest")
		.add("Lungs")
		.add("Heart");

		chest.get("Lungs")
		.add("Right Lung")
		.add("Left Lung");

		chest.get("Lungs").get("Right Lung")
		.add("Superior Lobe")
		.add("Middle Lobe")
		.add("Inferior Lobe");

		chest.get("Lungs").get("Left Lung")
		.add("Superior Lobe")
		.add("Inferior Lobe");

		chest.get("Heart")
		.add("Left Ventricle")
		.add("Right Ventricle")
		.add("Left Atrium")
		.add("Right Atrium")
		.add("Septum");


		//Show the GUI
		javax.swing.SwingUtilities.invokeLater(() -> {
			Solution<String> solution = new Solution<>(chest);
			solution.createAndShowGUI();
		});
	}
}