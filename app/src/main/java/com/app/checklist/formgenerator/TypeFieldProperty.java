package com.app.checklist.formgenerator;

public enum TypeFieldProperty {NONE,Type,Priority,Default,Modifiers,Options,Meta,Hint,Toggles,DateFormat,Table,Fields,Title,PrimaryKey,Visible,Editable,Remember,CodeReg,ReadOnly,TextColor;

	public static TypeFieldProperty parse(String val)
	{
		for (TypeFieldProperty type: TypeFieldProperty.values()) {
			if(val.equals(type.toString()))
				return type;
		}
		
		return NONE;	
	}
	
	public boolean EqualTo(String val)
	{
		if(val!=null && val.length()>0)
		{
			TypeFieldProperty fv=TypeFieldProperty.parse(val);
			if(this.equals(fv))
				return true;
		}
		return false;
	}


}
