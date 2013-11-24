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

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.view.Display;

public class Piala extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mHome, mMenu, mDebt1, mMaster1, mLight1, mDebt2, mMaster2, mLight2;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, home, menu, debt, master, light;
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
							return getAssets().open("asset/piala_menu.png");
						}
					});
			
			ITexture debt1 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/debt_no.png");
						}
					});
			ITexture debt2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/debt_ok.png");
						}
					});
			ITexture master1 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/lm_no.png");
						}
					});
			ITexture master2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/lm_ok.png");
						}
					});
			ITexture lg1 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/lightning_no.png");
						}
					});
			ITexture lg2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/lightning_ok.png");
						}
					});
			
			
			home_asset.load();
			menu_asset.load();
			debt1.load();
			debt2.load();
			master1.load();
			master2.load();
			lg1.load();
			lg2.load();
			
			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			mDebt1 = TextureRegionFactory.extractFromTexture(debt1);
			mDebt2 = TextureRegionFactory.extractFromTexture(debt2);
			mMaster1 = TextureRegionFactory.extractFromTexture(master1);
			mMaster2 = TextureRegionFactory.extractFromTexture(master2);
			mLight1 = TextureRegionFactory.extractFromTexture(lg1);
			mLight2 = TextureRegionFactory.extractFromTexture(lg2);

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


		if(Menu.player.isDebt) debt = new Sprite(0, 0, mDebt2, getVertexBufferObjectManager());
		else debt = new Sprite(0, 0, mDebt1, getVertexBufferObjectManager());
		debt.setScaleCenter(0, 0);
		debt.setScale(Menu.player.scale);
		debt.setPosition(cameraWidth / 6f, Menu.player.getCameraHeight(1/4.0));
		scene.attachChild(debt);
		
		if(Menu.player.isLightning) light = new Sprite(0, 0, mLight2, getVertexBufferObjectManager());
		else light = new Sprite(0, 0, mLight1, getVertexBufferObjectManager());
		light.setScaleCenter(0, 0);
		light.setScale(Menu.player.scale);
		light.setPosition(cameraWidth / 6f, Menu.player.getCameraHeight(1/2.0));
		scene.attachChild(light);
		
		if(Menu.player.isMaster) master = new Sprite(0, 0, mMaster2, getVertexBufferObjectManager());
		else master = new Sprite(0, 0, mMaster1, getVertexBufferObjectManager());
		master.setScaleCenter(0, 0);
		master.setScale(Menu.player.scale);
		master.setPosition(cameraWidth / 6f, Menu.player.getCameraHeight(3/4.0));
		scene.attachChild(master);
		
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

		scene.attachChild(home);
		scene.registerTouchArea(home);
		return scene;
	}

}
