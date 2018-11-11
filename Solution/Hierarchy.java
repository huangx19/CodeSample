
package Solution;

import java.util.*;

/**
*The Hierarchy data structure.
*A Hierarchy can have 0 or 1 parent, which is also an Hierarchy.
*A Hierarchy can have 0 or more childrens, which are also Hierarchy's
*
*
*@author X.Huang
*@version 1.0
*@since 2018-11-10
*/

public class Hierarchy<T> {

	private T mData;
	private Hierarchy<T> mParent;
	private Collection<Hierarchy<T>> mChildren;
	private String mId;

	/**
	*@return the data held by this Hierarchy
	*/
	public T data() {
		return mData;
	}

	/**
	*@param root the data to be held by the root hierachy
	*
	*@exception NullPointerException On <code>root == null</code>
	*/
	public Hierarchy(T root) {
		if (root == null)
			throw new NullPointerException();

		mData = root;
		mId = mData.toString();
	}

	/**
	*@return <code>Collection<Hierarcy<T>></code> the direct children of this Hierarchy;
	*			<code>null</code> if this Hierarchy doesn't have children
	*/
	public Collection<Hierarchy<T>> children() {
		if (mChildren != null)
			return new ArrayList<>(mChildren);

		return null;
	}

	/**
	*@return   the parent of this Hierarchy;
	*			<code>null</code> if this Hierarchy is the root;
	*/
	public Hierarchy<T> parent() {
		return mParent;
	}

	/**
	*@param id the unique identifier of a Hierarchy to be searched. The id must be generate by the id() method
	*			of this Hierarchy or one of its children or grandchildren. The id is unique within this Hierarchy
	*
	*@return the Hierarchy with the unique identifier id;
	*			<code>null</code> if no Hierarchy has this id
	*
	*@exception NullPointerException On id is <code>null</code>
	*/
	public Hierarchy<T> getById(String id) {
		if (id == null)
			throw new NullPointerException();

		if (id().equals(id))
			return this;

		Hierarchy<T> target = null;
		if (mChildren != null) {
			for (Hierarchy<T> child : mChildren) {
				target = child.getById(id);
				if (target != null)
					return target;
			}
		}
		return target;
	}

	/**
	*Search for a Hierarchy holding the same data. The data must implement <code>Object.Equals</code> method.
	*
	*@param data the data of the Hierarchy in searched
	*
	*@return the Hierarchy with the same data; If there are multiple Hierarchy's have the same data, only one of 
	*			them will be returned.
	*
	*@exception NullPointerException On data is null;
	*/
	public Hierarchy<T> get(T data) {
		if (data == null)
			throw new NullPointerException();

		if (data().equals(data))
			return this;

		Hierarchy<T> target = null;
		if (mChildren != null) {
			for (Hierarchy<T> child : mChildren) {
				target = child.get(data);
				if (target != null)
					return target;
			}
		}

		return target;
	}

	/**
	*Get the unique identifier of this Hierarchy. The id is unique in the whold hierarchy tree.
	*The id can be used to search for this Hierarchy from one of it's parent or grandparent Hierarchy's.
	*
	*@return the unique identifier of this Hierarchy
	*/
	public String id(){
		return mId;
	}

	/**
	* Add a new Hierarchy as a direct child of the current Hierarchy.
	*
	*@param data  the data to be held by the child Hierarchy
	*
	*@return the current Hierarchy
	*
	*@exception NullPointerException On data is null
	*/
	public Hierarchy<T> add(T data) {
		if (data == null)
			throw new NullPointerException();

		Hierarchy<T> child = new Hierarchy<>(data);
		if (mChildren == null)
			mChildren = new ArrayList<>();
		mChildren.add(child);
		child.mParent = this;
		child.mId = mId + "," + child.data().toString();

		return this;
	}
}