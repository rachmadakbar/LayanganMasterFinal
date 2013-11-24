package game;

import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.content.Intent;
import android.view.Display;

public class PilihLayangan extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mHome, mMenu, mK1, mK2, mK3;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, home, menu, k1, k2, k3;
	
	float rachmad = 0.8f;

	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();
		final Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						cameraWidth, cameraHeight), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "submenu.png",
						0, 0);
		try {

			ITexture home_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/home.png");
						}
					});
			ITexture menu_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/pilih_layangan.png");
						}
					});

			ITexture asset_k1 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite1.png");
						}
					});

			ITexture asset_k2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite2.png");
						}
					});

			ITexture asset_k3 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kite3.png");
						}
					});


			menu_asset.load();
			home_asset.load();
			asset_k1.load();
			asset_k2.load();
			asset_k3.load();
						
			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			mK1 = TextureRegionFactory.extractFromTexture(asset_k1);
			mK2 = TextureRegionFactory.extractFromTexture(asset_k2);
			mK3 = TextureRegionFactory.extractFromTexture(asset_k3);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		final int centerX = (int) ((cameraWidth - mBackgroundTextureRegion
				.getWidth()) / 2);
		final int centerY = (int) ((cameraHeight - mBackgroundTextureRegion
				.getHeight()) / 2);

		background = new Sprite(centerX, centerY, mBackgroundTextureRegion,
				getVertexBufferObjectManager());
		background.setScale(Menu.player.scale);
		scene.attachChild(background);

		if (Menu.player.scale != rachmad) {
			menu = new Sprite(0, 0, mMenu, getVertexBufferObjectManager());
			menu.setScaleCenter(0, 0);
			menu.setScale(Menu.player.scale);
			menu.setPosition(0, Menu.player.paddingY);

		} else {
			menu = new Sprite(0, Menu.player.paddingY, mMenu,
					getVertexBufferObjectManager());
		}
		scene.attachChild(menu);

		
		k1 = new Sprite(0, 0, mK1, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					Debug.e("kite", "kite1");
					Menu.player.setKite(1);
					startActivity(new Intent(getApplicationContext(),
							Test.class));
					finish();
					
				}
				return true;
			};
		};


		if (Menu.player.scale != rachmad) {
			k1.setScaleCenter(0, 0);
			k1.setScale(Menu.player.scale);
		}

		k1.setPosition(cameraWidth*0.25f, Menu.player.getCameraHeight(0.4));
		scene.attachChild(k1);
		scene.registerTouchArea(k1);

		k2 = new Sprite(0, 0, mK2, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					Debug.e("kite", "kite2");
					Menu.player.setKite(2);
					startActivity(new Intent(getApplicationContext(),
							Test.class));
					finish();
				}
				return true;
			};
		};

		if (Menu.player.scale != rachmad) {
			k2.setScaleCenter(0, 0);
			k2.setScale(Menu.player.scale);
		}

		k2.setPosition(cameraWidth*0.45f, Menu.player.getCameraHeight(0.4));

		
		k3 = new Sprite(0, 0, mK3, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					Debug.e("kite", "kite3");
					Menu.player.setKite(3);
					startActivity(new Intent(getApplicationContext(),
							Test.class));
					finish();
				}
				return true;
			};
		};

		if (Menu.player.scale != rachmad) {
			k3.setScaleCenter(0, 0);
			k3.setScale(Menu.player.scale);
		}

		k3.setPosition(cameraWidth*0.7f, Menu.player.getCameraHeight(0.4));
		
		if(Menu.player.buyGreen){
			scene.attachChild(k3);
			scene.registerTouchArea(k3);
		}
		
		if(Menu.player.buyPurple){
			scene.attachChild(k2);
			scene.registerTouchArea(k2);
		}
		
		home = new Sprite(0, 0, mHome, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					finish();
				}
				return true;
			};
		};

		// if(Menu.player.scale!=0.8f){
		home.setScaleCenter(0, 0);
		home.setScale(Menu.player.scale);
		// }
		home.setPosition(cameraWidth * 7 / 8,
				Menu.player.getCameraHeight(4 / 5.0));

		scene.registerTouchArea(home);
		scene.attachChild(home);
		
		
		
		return scene;
	}
	
	@Override
	protected void onDestroy()
	{
	    super.onDestroy();
	        
	    if (this.isGameLoaded())
	    {
	        System.exit(0);    
	    }
	}
}
