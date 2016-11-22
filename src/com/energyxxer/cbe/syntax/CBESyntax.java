package com.energyxxer.cbe.syntax;

import java.util.HashMap;

import com.energyxxer.cbe.syntax.style.Style;

/**
 * Defines what and how character sequences should be highlighted in a text
 * editor.
 */
public class CBESyntax extends Syntax {
	public static HashMap<String, HashMap<String, Object>> styles = new HashMap<String, HashMap<String, Object>>();

	public static final CBESyntax INSTANCE = new CBESyntax();
	
	protected CBESyntax() {super();}

	static {
		styles.put("comment", new Style().setColor("#777777").setItalic().getMap());
		styles.put("multilinecomment", new Style().setColor("#777777").setItalic().getMap());
		styles.put("number", new Style().setColor("#E89089").getMap());
		styles.put("qualifier", new Style().setColor("#6655CC").setBold().getMap());
		styles.put("unit_type", new Style().setColor("#6655CC").setBold().getMap());
		styles.put("unit_action", new Style().setColor("#6655CC").setBold().getMap());
		styles.put("data_type", new Style().setColor("#6655CC").setBold().getMap());
		styles.put("keyword", new Style().setColor("#6655CC").setBold().getMap());
		styles.put("action_keyword", new Style().setColor("#6655CC").setBold().getMap());
		styles.put("boolean", new Style().setColor("#6655CC").setBold().setItalic().getMap());
		styles.put("string_literal", new Style().setColor("#668844").getMap());
		styles.put("operator", new Style().setColor("#A72D2D").getMap());
		styles.put("negation_operator", new Style().setColor("#A72D2D").getMap());
		styles.put("identifier_operator", new Style().setColor("#A72D2D").getMap());
		styles.put("null", new Style().setColor("#880000").setBold().getMap());

		styles.put("#is_entity", new Style().setColor("#447722").getMap());
		styles.put("#is_enum", new Style().setColor("#BB44AA").getMap());
		styles.put("#is_enum_value", new Style().setColor("#000088").setBold().getMap());
		styles.put("#is_annotation", new Style().setColor("#6A6A6A").setItalic().getMap());

	}

	@Override
	public HashMap<String, HashMap<String, Object>> getStyles() {
		return styles;
	}
}