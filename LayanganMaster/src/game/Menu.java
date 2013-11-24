package game;

import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import helper.LayanganMasterDBAdapter;
import helper.Player;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.content.Intent;
import android.view.Display;

public class Menu extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mMain, mPengaturan, mMenu,
			mBantuan, mPiala, mBengkel;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, main, pengaturan, menu, piala, bantuan, bengkel;
	static Music music;
	static Player player;
	
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
		engineOptions.getAudioOptions().setNeedsMusic(true)
					.setNeedsSound(true);
		
		player = new Player(getApplicationContext());
		player.setScale(cameraWidth/400f);
		player.setPaddingY((float)(cameraHeight * Math.ceil(player.scale) - cameraHeight
				* player.scale) / 2);
		
		
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("story/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "sc1.png", 0,
						0);
		try {

			ITexture menu_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/menu.png");
						}
					});
			ITexture main_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/main.png");
						}
					});
			ITexture bantuan_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/help.png");
						}
					});
			ITexture pengaturan_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/setting.png");
						}
					});
			ITexture piala_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/piala.png");
						}
					});
			ITexture bengkel_asset = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/bengkel.png");
						}
					});

			menu_asset.load();
			main_asset.load();
			piala_asset.load();
			pengaturan_asset.load();
			bantuan_asset.load();
			bengkel_asset.load();

			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			mBantuan = TextureRegionFactory.extractFromTexture(bantuan_asset);
			mMain = TextureRegionFactory.extractFromTexture(main_asset);
			mPengaturan = TextureRegionFactory
					.extractFromTexture(pengaturan_asset);
			mPiala = TextureRegionFactory.extractFromTexture(piala_asset);
			mBengkel = TextureRegionFactory.extractFromTexture(bengkel_asset);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);

		try {
			music = MusicFactory
					.createMusicFromAsset(mEngine.getMusicManager(), this,
							"msc/background_music.wav");
			music.setLooping(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	int i = 0;

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		final int centerX = (int) ((cameraWidth - mBackgroundTextureRegion
				.getWidth()) / 2);
		final int centerY = (int) ((cameraHeight - mBackgroundTextureRegion
				.getHeight()) / 2);
		background = new Sprite(centerX, centerY, mBackgroundTextureRegion,
				getVertexBufferObjectManager());
		background.setScale(player.scale);
		scene.attachChild(background);

		

		if (player.scale != 0.8f) {
			menu = new Sprite(centerX, centerY, mMenu,
					getVertexBufferObjectManager());
			menu.setScale(player.scale);
		} else {
			menu = new Sprite(0, player.paddingY, mMenu,
					getVertexBufferObjectManager());
		}
		scene.attachChild(menu);

		main = new Sprite(0, 0, mMain, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					startActivity(new Intent(getApplicationContext(),
							PilihLayangan.class));
				}
				return true;
			};
		};

		main.setScaleCenter(0, 0);
		main.setScale(player.scale);
		main.setPosition(cameraWidth / 4f, player.getCameraHeight(1/4.0));
		scene.registerTouchArea(main);
		scene.attachChild(main);

		bantuan = new Sprite(0, 0, mBantuan,
				getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					startActivity(new Intent(getApplicationContext(),
							Status.class));
				}
				return true;
			};
		};
		
		if (player.scale != 0.8f){
			bantuan.setScaleCenter(0, 0);
			bantuan.setScale(player.scale);
		}
			
		bantuan.setPosition(cameraWidth/12f, player.getCameraHeight(3/4.0));
		scene.registerTouchArea(bantuan);
		scene.attachChild(bantuan);

		pengaturan = new Sprite(0, 0,
				mPengaturan, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					startActivity(new Intent(getApplicationContext(),
							SettingActivity.class));

				}
				return true;
			};
		};
		
		if (player.scale != 0.8f){
			pengaturan.setScaleCenter(0, 0);
			pengaturan.setScale(player.scale);
		}
		
		pengaturan.setPosition(cameraWidth-cameraWidth/12f-pengaturan.getWidthScaled(), player.getCameraHeight(3/4.0));
		scene.registerTouchArea(pengaturan);
		scene.attachChild(pengaturan);

		piala = new Sprite(0, 0, mPiala, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					startActivity(new Intent(getApplicationContext(),
							Piala.class));
				}
				return true;
			};
		};

		piala.setScaleCenter(0, 0);
		piala.setScale(player.scale);
		piala.setPosition(cameraWidth / 4f, player.getCameraHeight(1/2.0));
		scene.registerTouchArea(piala);
		scene.attachChild(piala);

		bengkel = new Sprite(0, 0, mBengkel, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					startActivity(new Intent(getApplicationContext(),
							Bengkel.class));
				}
				return true;
			};
		};

		bengkel.setScaleCenter(0, 0);
		bengkel.setScale(player.scale);
		bengkel.setPosition(cameraWidth / 4f, player.getCameraHeight(3/4.0));
		scene.registerTouchArea(bengkel);
		scene.attachChild(bengkel);
		
		mEngine.registerUpdateHandler(new TimerHandler(1f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						
						if(player.isSound){
							music.play();
						}
						
					}
				}));

		return scene;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (this.isGameLoaded()) {
			System.exit(0);
		}
	}
}
