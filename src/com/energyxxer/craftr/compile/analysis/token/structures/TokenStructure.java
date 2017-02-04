package com.energyxxer.craftr.compile.analysis.token.structures;

import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.util.StringBounds;
import com.energyxxer.craftr.util.StringLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TokenStructure extends TokenPattern<TokenPattern<?>> {
	private TokenPattern<?> group;
	
	public TokenStructure(String name, TokenPattern<?> group) {
		this.name = name;
		this.group = group;
	}

	@Override
	public TokenPattern<?> getContents() {
		return group;
	}
	
	@Override
	public TokenStructure setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String toString() {
		return name + ": {" + group.toString() + "}";
	}

	@Override
	public List<Token> search(String type) {
		return group.search(type);
	}

	@Override
	public List<Token> deepSearch(String type) {
		return group.deepSearch(type);
	}

	@Override
	public List<TokenPattern<?>> searchByName(String name) {
		return group.searchByName(name);
	}

	@Override
	public List<TokenPattern<?>> deepSearchByName(String name) {
		ArrayList<TokenPattern<?>> list = new ArrayList<>();
		if(group.name.equals(name)) list.add(group);
		//if(this.name.equals(name)) list.add(this);
		list.addAll(group.deepSearchByName(name));
		return list;
	}

	@Override
	public String flatten(boolean separate) {
		return group.flatten(separate);
	}

	@Override
	public File getFile() {
		return group.getFile();
	}

	@Override
	public StringLocation getStringLocation() {
		return group.getStringLocation();
	}

	@Override
	public StringBounds getStringBounds() { return group.getStringBounds(); }

    @Override
    public ArrayList<Token> flattenTokens() {
        return group.flattenTokens();
    }

	@Override
	public String getType() {
		return "STRUCTURE";
	}
}