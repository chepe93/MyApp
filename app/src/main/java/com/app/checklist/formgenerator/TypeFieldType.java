package com.app.checklist.formgenerator;

public enum TypeFieldType {NONE,Integer,String,Text,Boolean,Numeric,Date,Time,Image;


	public static TypeFieldType parse(String val)
	{
		for (TypeFieldType type: TypeFieldType.values()) {
			if(val.equals(type.toString()))
				return type;
		}
		
		return NONE;	
	}
	
	public boolean EqualTo(String val)
	{
		if(val!=null && val.length()>0)
		{
			TypeFieldType fv=TypeFieldType.parse(val);
			if(this.equals(fv))
				return true;
		}
		return false;
	}


}
