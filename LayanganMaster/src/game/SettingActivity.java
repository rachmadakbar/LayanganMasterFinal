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

public class SettingActivity extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mHome, mSoundOn, mSoundOff, mCameraOn, mCameraOff, mMenu;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, home, soundOn, soundOff, cameraOn, cameraOff, menu;
	float rachmad = 0.8f;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();
		final Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(cameraWidth, cameraHeight), camera);
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
				.createFromAsset(this.mBitmapTextureAtlas, this, "submenu.png", 0,
						0);
		try {
			
			ITexture soundOn_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/soundOn.png");
						}
					});
			ITexture soundOff_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/soundOff.png");
						}
					});
			ITexture cameraOn_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/cameraOn.png");
						}
					});
			ITexture cameraOff_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/cameraOff.png");
						}
					});
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
							return getAssets().open("asset/pengaturan_menu.png");
						}
					});
			
			soundOff_asset.load();
			soundOn_asset.load();
			cameraOn_asset.load();
			cameraOff_asset.load();
			home_asset.load();
			menu_asset.load();
			
			mSoundOff = TextureRegionFactory.extractFromTexture(soundOff_asset);
			mSoundOn = TextureRegionFactory.extractFromTexture(soundOn_asset);
			mCameraOn = TextureRegionFactory.extractFromTexture(cameraOn_asset);
			mCameraOff = TextureRegionFactory.extractFromTexture(cameraOff_asset);
			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		
	}

	int i = 0;

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		final int centerX = (int) ((cameraWidth - mBackgroundTextureRegion
				.getWidth()) / 2);
		final int centerY = (int) ((cameraHeight - mBackgroundTextureRegion
				.getHeight()) / 2);
		background = new
				Sprite(centerX, centerY,mBackgroundTextureRegion,getVertexBufferObjectManager());
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
		
		
		float sOnX,sOffX;
		if(Menu.player.isSound){
			sOnX = cameraWidth/5;
			sOffX = -cameraWidth;
		}else {
			sOnX = -cameraWidth;
			sOffX = cameraWidth/5;
		}
		
		soundOn = new
				Sprite(0,0,mSoundOn,getVertexBufferObjectManager())
		{
		    @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionUp())
		        {
		        	Menu.music.pause();
		        	soundOn.setPosition(-cameraWidth, Menu.player.getCameraHeight(1/4.0));
		        	soundOff.setPosition(cameraWidth/5,Menu.player.getCameraHeight(1/4.0));
		    		Menu.player.setSound(0);
		        }
		        return true;
		    };
		};
		
		if(Menu.player.scale!=rachmad){
			soundOn.setScaleCenter(0, 0);
			soundOn.setScale(Menu.player.scale);
		}
		soundOn.setPosition(sOnX, Menu.player.getCameraHeight(1/4.0));
		scene.registerTouchArea(soundOn);
		scene.attachChild(soundOn);
		
		soundOff = new
				Sprite(0,0,mSoundOff,getVertexBufferObjectManager())
		{
		    @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionUp())
		        {
		        	Menu.music.play();
		        	soundOff.setPosition(-cameraWidth, Menu.player.getCameraHeight(1/4.0));
		        	soundOn.setPosition(cameraWidth/5, Menu.player.getCameraHeight(1/4.0));
		        	Menu.player.setSound(1);
		        }
		        return true;
		    };
		};
		
		if(Menu.player.scale!=rachmad){
			soundOff.setScaleCenter(0, 0);
			soundOff.setScale(Menu.player.scale);
		}
		soundOff.setPosition(sOffX, Menu.player.getCameraHeight(1/4.0));
		
		scene.registerTouchArea(soundOff);
		scene.attachChild(soundOff);
		
		float cOnX,cOffX;
		
		if(Menu.player.isCamera){
			cOnX = cameraWidth*3/5;
			cOffX = -cameraWidth;
		}else {
			cOffX = cameraWidth*3/5;
			cOnX = -cameraWidth;
		}
		cameraOn = new
				Sprite(0,0,mCameraOn,getVertexBufferObjectManager())		{
		    @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionUp())
		        {
		        	cameraOn.setPosition(-cameraWidth, Menu.player.getCameraHeight(1/4.0));
		        	cameraOff.setPosition(cameraWidth*3/5, Menu.player.getCameraHeight(1/4.0));
		    		Menu.player.setCamera(0);
		        }
		        return true;
		    };
		};
		
		if(Menu.player.scale!=rachmad){
			cameraOn.setScaleCenter(0, 0);
			cameraOn.setScale(Menu.player.scale);
		}
		cameraOn.setPosition(cOnX, Menu.player.getCameraHeight(1/4.0));
		
		scene.registerTouchArea(cameraOn);	
		scene.attachChild(cameraOn);
		
		cameraOff = new
				Sprite(0,0,mCameraOff,getVertexBufferObjectManager())		{
		    @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionUp())
		        {
		        	cameraOff.setPosition(-cameraWidth, Menu.player.getCameraHeight(1/4.0));
		        	cameraOn.setPosition(cameraWidth*3/5, Menu.player.getCameraHeight(1/4.0));
		        	Menu.player.setCamera(1);
		        }
		        return true;
		    };
		};
		
		if(Menu.player.scale!=rachmad){
			cameraOff.setScaleCenter(0, 0);
			cameraOff.setScale(Menu.player.scale);
		}
		cameraOff.setPosition(cOffX, Menu.player.getCameraHeight(1/4.0));
		scene.registerTouchArea(cameraOff);	
		scene.attachChild(cameraOff);
		
		home = new
				Sprite(0, 0,mHome,getVertexBufferObjectManager())		{
		    @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionUp())
		        {
		        	finish();
		        }
		        return true;
		    };
		};
		
		//if(Menu.player.scale!=0.8f){
			home.setScaleCenter(0, 0);
			home.setScale(Menu.player.scale);
		//}
		home.setPosition(cameraWidth*7/8, Menu.player.getCameraHeight(4/5.0));
		
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

