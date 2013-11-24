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


import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;
import android.view.Display;

public class EndStory extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mLayangan, mLove;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, love, layangan;
	Music music;
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
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("story/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "sc18.png", 0,
						0);
		try {
			
			ITexture sc19 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc19.png");
						}
					});
			ITexture sc20 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc20.png");
						}
					});
			
			sc19.load();
			sc20.load();
			
			mLove = TextureRegionFactory.extractFromTexture(sc20);
			mLayangan = TextureRegionFactory.extractFromTexture(sc19);		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		 try
	        {
	            music = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this,"msc/Benteng_Master-Opening.mp3");
	            music.setLooping(true);
	        }
	        catch (IOException e)
	        {
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
		background = new
				Sprite(centerX, centerY,mBackgroundTextureRegion,getVertexBufferObjectManager());
		scene.attachChild(background);
		music.play();

		mEngine.registerUpdateHandler(new TimerHandler(3f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						if (i == 0) {
							
							layangan = new Sprite(centerX, centerY, mLayangan,
									getVertexBufferObjectManager());
							scene.attachChild(layangan);
							i++;
						}else if(i == 1){
							 love = new Sprite(centerX, centerY, mLove,
									getVertexBufferObjectManager());
							scene.detachChild(layangan);
							scene.attachChild(love);
						}else if(i == 2){
							i++;
							music.stop();
							mEngine.unregisterUpdateHandler(pTimerHandler);
						}
					}
				}));

		return scene;
	}

}

