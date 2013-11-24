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

public class Status extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mHome, mMenu;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, home, menu;
	private Font  mFont;
	private Text  attack,hp,exp,speed,defense,level;
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
							return getAssets().open("asset/status_menu.png");
						}
					});

			
			menu_asset.load();
			home_asset.load();
			
			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			
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
		
		hp = new Text(cameraWidth*0.05f, Menu.player.getCameraHeight(0.3), mFont, "HP       : "+Menu.player.HP, getVertexBufferObjectManager());
		hp.setScaleCenter(0, 0);
		hp.setScale(Menu.player.scale);
		scene.attachChild(hp);

		exp = new Text(cameraWidth*0.05f, Menu.player.getCameraHeight(0.5), mFont, "EXP     : "+Menu.player.exp, getVertexBufferObjectManager());
		exp.setScaleCenter(0, 0);
		exp.setScale(Menu.player.scale);
		scene.attachChild(exp);
		
		level = new Text(cameraWidth*0.05f, Menu.player.getCameraHeight(0.7), mFont, "LEVEL : "+Menu.player.level, getVertexBufferObjectManager());
		level.setScaleCenter(0, 0);
		level.setScale(Menu.player.scale);
		scene.attachChild(level);
		
		attack = new Text(cameraWidth*0.5f, Menu.player.getCameraHeight(0.3), mFont, "ATTACK  : "+Menu.player.attack, getVertexBufferObjectManager());
		attack.setScaleCenter(0, 0);
		attack.setScale(Menu.player.scale);
		scene.attachChild(attack);

		defense = new Text(cameraWidth*0.5f, Menu.player.getCameraHeight(0.5), mFont, "DEFENSE : "+Menu.player.defense, getVertexBufferObjectManager());
		defense.setScaleCenter(0, 0);
		defense.setScale(Menu.player.scale);
		scene.attachChild(defense);
		
		speed = new Text(cameraWidth*0.5f, Menu.player.getCameraHeight(0.7), mFont, "SPEED     : "+Menu.player.speed, getVertexBufferObjectManager());
		speed.setScaleCenter(0, 0);
		speed.setScale(Menu.player.scale);
		scene.attachChild(speed);
		
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
