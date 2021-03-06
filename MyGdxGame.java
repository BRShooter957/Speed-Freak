
package com.lira.speedfreak;


import com.badlogic.gdx.Game;
//import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MyGdxGame extends Game {

	private final AssetManager assetManager = new AssetManager();

	@Override
	public void create() {
		assetManager.setLoader(TiledMap.class,
				new TmxMapLoader(new InternalFileHandleResolver()));

		//SETS THE START SCREEN
		setScreen(new StartScreen(this));
	}

	public AssetManager getAssetManager() {
		return assetManager;

	}
}
