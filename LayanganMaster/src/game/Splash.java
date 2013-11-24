package game;

import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import helper.LayanganMasterDBAdapter;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;

import android.content.Intent;
import android.view.Display;

public class Splash extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	Sprite background;
	float scale;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();
		scale = cameraWidth/400f;
		
		final Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						cameraWidth, cameraHeight), camera);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		try {
			ITexture logo = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("logo/logo.png");
						}
					});
			logo.load();

			mBackgroundTextureRegion = TextureRegionFactory
					.extractFromTexture(logo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if(scale!=0.8f) 
		background.setScale(scale);
		scene.attachChild(background);
		mEngine.registerUpdateHandler(new TimerHandler(0.9f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						
						mEngine.unregisterUpdateHandler(pTimerHandler);
						LayanganMasterDBAdapter db = new LayanganMasterDBAdapter(getApplicationContext());
						db.open();
						//db.dropDatabase();
						boolean isNew = db.isNew("rabbit");
						if(isNew){
							db.close();
							startActivity(new Intent(getApplicationContext(),
								Menu.class));
							finish();
						}else {
							db.close();
							startActivity(new Intent(getApplicationContext(),
									Menu.class));
								finish();
						}
								db.close();
					}
				}));

		return scene;
	}
}