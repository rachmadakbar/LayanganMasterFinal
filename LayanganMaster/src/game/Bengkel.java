package game;

import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.view.Display;

public class Bengkel extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mHome, mMenu, m10, m15,
			m30, md, mCoin;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, home, menu, h10, h15, h30, hd, coin;
	private Font  mFont;
	private Text  text;
	
	float rachmad = 10.8f;

	
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
							return getAssets().open("asset/bengkel_menu.png");
						}
					});

			ITexture asset_10 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/1000.png");
						}
					});

			ITexture asset_15 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/1500.png");
						}
					});

			ITexture asset_30 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/3000.png");
						}
					});

			ITexture asset_d = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/kejual.png");
						}
					});
			ITexture asset_coin = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/coin2.png");
						}
					});


			menu_asset.load();
			home_asset.load();
			asset_10.load();
			asset_15.load();
			asset_30.load();
			asset_d.load();
			asset_coin.load();
			
			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			m10 = TextureRegionFactory.extractFromTexture(asset_10);
			m15 = TextureRegionFactory.extractFromTexture(asset_15);
			m30 = TextureRegionFactory.extractFromTexture(asset_30);
			md = TextureRegionFactory.extractFromTexture(asset_d);
			mCoin = TextureRegionFactory.extractFromTexture(asset_coin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		FontFactory.setAssetBasePath("font/");

		mFont = FontFactory.createFromAsset(mEngine.getFontManager(),
		            mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
		            this.getAssets(), "RAVIE.TTF", 20f, true,
		            Color.WHITE_ABGR_PACKED_INT);
		mFont.load();
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

		if (Menu.player.buyBlue) {
			h10 = new Sprite(0, 0, md, getVertexBufferObjectManager());
		} else {
			h10 = new Sprite(0, 0, m10, getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float X, float Y) {
					if (pSceneTouchEvent.isActionUp()) {
						if (Menu.player.coin >= 1000) {
							Menu.player.updateCoin(Menu.player.coin-1000);
							text.setText(Menu.player.coin+"");
							Menu.player.beli("blue");
							scene.detachChild(h10);
							h10 = new Sprite(0, 0, md,
									getVertexBufferObjectManager());
							float padding1 = 30;
							if (Menu.player.scale != rachmad) {
								h10.setScaleCenter(0, 0);
								h10.setScale(Menu.player.scale);
								padding1 = 0;
							}

							h10.setPosition(cameraWidth * 0.6f,
									Menu.player.getCameraHeight(0.65)
											+ padding1);
							scene.attachChild(h10);
							scene.unregisterTouchArea(h10);
						}
					}
					return true;
				};
			};
		}

		float padding1 = 30;
		if (Menu.player.scale != rachmad) {
			h10.setScaleCenter(0, 0);
			h10.setScale(Menu.player.scale);
			padding1 = 0;
		}

		h10.setPosition(cameraWidth * 0.6f, Menu.player.getCameraHeight(0.65)
				+ padding1);
		scene.attachChild(h10);
		scene.registerTouchArea(h10);

		if (Menu.player.buyPurple) {
			h15 = new Sprite(0, 0, md, getVertexBufferObjectManager());
		} else {
			h15 = new Sprite(0, 0, m15, getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float X, float Y) {
					if (pSceneTouchEvent.isActionUp()) {
						if (Menu.player.coin >= 1500) {
							Menu.player.updateCoin(Menu.player.coin-1500);
							text.setText(Menu.player.coin+"");
							Menu.player.beli("purple");
							scene.detachChild(h15);
							h15 = new Sprite(0, 0, md,
									getVertexBufferObjectManager());
							float padding2 = 10;
							if (Menu.player.scale != rachmad) {
								h15.setScaleCenter(0, 0);
								h15.setScale(Menu.player.scale);
								padding2 = 0;
							}
							h15.setPosition(cameraWidth * 0.6f,
									Menu.player.getCameraHeight(0.25)
											+ padding2);
							scene.attachChild(h15);
							scene.unregisterTouchArea(h15);
						}
					}
					return true;
				};
			};
		}
		float padding2 = 10;
		if (Menu.player.scale != rachmad) {
			h15.setScaleCenter(0, 0);
			h15.setScale(Menu.player.scale);
			padding2 = 0;
		}

		h15.setPosition(cameraWidth * 0.6f, Menu.player.getCameraHeight(0.25)
				+ padding2);
		scene.registerTouchArea(h15);
		scene.attachChild(h15);

		if (Menu.player.buyGreen) {
			h30 = new Sprite(0, 0, md, getVertexBufferObjectManager());
		} else {
			h30 = new Sprite(0, 0, m30, getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float X, float Y) {
					if (pSceneTouchEvent.isActionUp()) {
						if (Menu.player.coin >= 3000) {
							Menu.player.updateCoin(Menu.player.coin-3000);
							text.setText(Menu.player.coin+"");
							Menu.player.beli("green");
							scene.detachChild(h30);
							h30 = new Sprite(0, 0, md,
									getVertexBufferObjectManager());
							float padding3 = 20;
							if (Menu.player.scale != rachmad) {
								h30.setScaleCenter(0, 0);
								h30.setScale(Menu.player.scale);
								padding3 = 0;
							}

							h30.setPosition(cameraWidth * 0.6f, Menu.player.getCameraHeight(0.45)
									+ padding3);
							scene.attachChild(h30);
							scene.unregisterTouchArea(h30);
						}
					}
					return true;
				};
			};
		}
		float padding3 = 20;
		if (Menu.player.scale != rachmad) {
			h30.setScaleCenter(0, 0);
			h30.setScale(Menu.player.scale);
			padding3 = 0;
		}

		h30.setPosition(cameraWidth * 0.6f, Menu.player.getCameraHeight(0.45)
				+ padding3);
		scene.registerTouchArea(h30);
		scene.attachChild(h30);

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
		
		text = new Text(cameraWidth*0.8f, Menu.player.getCameraHeight(0.04), mFont, Menu.player.coin+"", getVertexBufferObjectManager());
		text.setScaleCenter(0, 0);
		text.setScale(Menu.player.scale);
		scene.attachChild(text);
		
		coin = new Sprite(cameraWidth*0.72f, Menu.player.getCameraHeight(0.04), mCoin, getVertexBufferObjectManager());
		coin.setScaleCenter(0, 0);
		coin.setScale(Menu.player.scale);
		scene.attachChild(coin);
		  
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
