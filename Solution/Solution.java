package Solution;

import javax.swing.*;
import javax.swing.Box.*;
import java.awt.*;

import java.util.*;

public class Solution {

	Map<String, JCheckBox> mMap = new HashMap<>();
	Hierarchy<String> mRoot;

	public Solution(Hierarchy<String> root){
		mRoot = root;
	}

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
			Solution solution = new Solution(chest);
			solution.createAndShowGUI();
		});
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
	private void addHierarchyToContainer(Hierarchy<String> hierarchy, Container container, int level) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JCheckBox checkBox = new JCheckBox(hierarchy.data());
		final String id = hierarchy.id();
		checkBox.addActionListener(
			event -> {
				JCheckBox source = (JCheckBox) event.getSource();
				onItemCheck(id, source.isSelected());
			});
		mMap.put(hierarchy.id(), checkBox);

		panel.add(Box.createRigidArea(new Dimension(level*20, 0)));
		panel.add(checkBox);
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		container.add(panel);

		if (hierarchy.children() != null) {
			for (Hierarchy<String> child : hierarchy.children())
				addHierarchyToContainer(child, panel, level+1);
		}
	}

	private void onItemCheck(String id, boolean checked){
		if (checked)
			checkItem(id);
		else
			uncheckItem(id);
	}

	//Recursively check an item and it parents
	private void checkItem(String id) {
		mMap.get(id).setSelected(true);
		Hierarchy<String> parent = mRoot.getById(id).parent();
		if (parent != null)
			checkItem(parent.id());
	}

	//Recursively uncheck and item and its children
	private void uncheckItem(String id) {
		mMap.get(id).setSelected(false);
		Collection<Hierarchy<String>> children = mRoot.getById(id).children();
		if (children != null)
			for (Hierarchy<String> child : children)
				uncheckItem(child.id());
	}
}