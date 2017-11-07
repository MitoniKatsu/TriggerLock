package edu.seminolestate.mitoni;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FieldLimit extends PlainDocument
{
	/**
	 * For character limits on JTextFields
	 */
	private static final long serialVersionUID = 1L;
	private int limit;
	private boolean toUppercase = false;

	FieldLimit(int limit) {
     super();
     this.limit = limit;
     }

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
	{
		if (str == null)
			return;

		if ((getLength() + str.length()) <= limit)
		{
			if (toUppercase)
				str = str.toUpperCase();
			super.insertString(offset, str, attr);
		}
	}
}
