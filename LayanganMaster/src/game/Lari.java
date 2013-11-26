package game;

import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
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
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.sensor.location.ILocationListener;
import org.andengine.input.sensor.location.LocationProviderStatus;
import org.andengine.input.sensor.location.LocationSensorOptions;
import org.andengine.input.touch.TouchEvent;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Display;

public class Lari extends SimpleBaseGameActivity implements SensorEventListener, ILocationListener {

	private ITextureRegion mBackgroundTextureRegion, mStart, mFinish;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, finish, start;
	private Font mFont;
	private Text count, lat1,lat2,long1,long2;
	private int tempX = 0;
	private int tempY = 0;
	private int accellerometerSpeedX;
	private int accellerometerSpeedY;
	private int pedo;
	private SensorManager sensorManager;
	private Location mUserLocation;
	private float latitude_start, longitude_start;
	private float latitude_finish, longitude_finish;
	float rachmad = 0.8f;
	boolean counterON = false;
	private static final double RADIUS_EARTH_METERS = 6371000;

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
		
		this.mUserLocation = new Location(LocationManager.GPS_PROVIDER);
		
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		sensorManager = (SensorManager) this
				.getSystemService(this.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sensorManager.SENSOR_DELAY_GAME);
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mEngine.registerUpdateHandler(new IUpdateHandler() {
			public void onUpdate(float pSecondsElapsed) {
				updateSpritePosition();
			}

			public void reset() {
				// TODO Auto-generated method stub
			}
		});
		
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "submenu.png",
						0, 0);
		try {

			ITexture start_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/lari.png");
						}
					});
			ITexture finish_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/terbangkan.png");
						}
					});

			start_asset.load();
			finish_asset.load();

			mStart = TextureRegionFactory.extractFromTexture(start_asset);
			mFinish = TextureRegionFactory.extractFromTexture(finish_asset);

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

	protected void updateSpritePosition() {
		// TODO Auto-generated method stub
		if(counterON) {
			if(Math.abs(accellerometerSpeedX-tempX)> 3 || Math.abs(accellerometerSpeedY-tempY)> 3){
				pedo++;
			}
		}
		
		//count.setText(""+pedo);
		
		tempX = accellerometerSpeedX;
		tempY = accellerometerSpeedY;
		
	}

	int i = 10;

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

		start = new Sprite(0, 0, mStart, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					
					latitude_start = (float)mUserLocation.getLatitude();
					longitude_start = (float) mUserLocation.getLongitude();
					
					counterON= true;
					mEngine.registerUpdateHandler(new TimerHandler(1f, true,
							new ITimerCallback() {
								@Override
								public void onTimePassed(TimerHandler pTimerHandler) {
									if (i >= 0) {
										
										count.setText("" + i--);
									} else {
										counterON = false;
										latitude_finish = (float)mUserLocation.getLatitude();
										longitude_finish = (float) mUserLocation.getLongitude();
										mEngine.unregisterUpdateHandler(pTimerHandler);
										scene.detachChild(start);
										scene.unregisterTouchArea(start);
										scene.registerTouchArea(finish);
										scene.attachChild(finish);
									}
								}
							}));
			
				
				}
				return true;
			};
		};

		if (Menu.player.scale != 0.8f) {
			start.setScaleCenter(0, 0);
			start.setScale(Menu.player.scale);
		}

		start.setPosition(cameraWidth / 2, Menu.player.getCameraHeight(0.7));
		scene.registerTouchArea(start);
		scene.attachChild(start);

		
		
		finish = new Sprite(0, 0, mFinish, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					//count.setText( latitude_start+" "+latitude_finish + " " + longitude_start+" "+longitude_finish);
					lat1.setText(latitude_start+"");
					lat2.setText(latitude_finish+"");
					long1.setText(longitude_start+"");
					long2.setText(longitude_finish+"");
					
					Debug.e("velo", latitude_start+" "+latitude_finish + " " + longitude_start+" "+longitude_finish);
				}
				return true;
			};
		};

		if (Menu.player.scale != 0.8f) {
			finish.setScaleCenter(0, 0);
			finish.setScale(Menu.player.scale);
		}

		finish.setPosition(cameraWidth / 2, Menu.player.getCameraHeight(0.7));
		
		
		count = new Text(cameraWidth*0.45f, Menu.player.getCameraHeight(0.4),
				mFont, "                                                                                          ", getVertexBufferObjectManager());
		count.setScaleCenter(0, 0);
		count.setScale(Menu.player.scale);
		scene.attachChild(count);

		lat1 = new Text(cameraWidth*0.05f, Menu.player.getCameraHeight(0.2),
				mFont, "                                                                                          ", getVertexBufferObjectManager());
		lat1.setScaleCenter(0, 0);
		lat1.setScale(Menu.player.scale);
		scene.attachChild(lat1);
		
		lat2 = new Text(cameraWidth*0.05f, Menu.player.getCameraHeight(0.4),
				mFont, "                                                                                          ", getVertexBufferObjectManager());
		lat2.setScaleCenter(0, 0);
		lat2.setScale(Menu.player.scale);
		scene.attachChild(lat2);
		
		long1 = new Text(cameraWidth*0.5f, Menu.player.getCameraHeight(0.2),
				mFont, "                                                                                          ", getVertexBufferObjectManager());
		long1.setScaleCenter(0, 0);
		long1.setScale(Menu.player.scale);
		scene.attachChild(long1);
		
		
		long2 = new Text(cameraWidth*0.5f, Menu.player.getCameraHeight(0.4),
				mFont, "                                                                                          ", getVertexBufferObjectManager());
		long2.setScaleCenter(0, 0);
		long2.setScale(Menu.player.scale);
		scene.attachChild(long2);
		
		return scene;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (this.isGameLoaded()) {
			System.exit(0);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				accellerometerSpeedX = (int) event.values[1];
				accellerometerSpeedY = (int) event.values[0];
				break;
			}
		}
		
	}

	@Override
	public void onLocationProviderEnabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location pLocation) {
		// TODO Auto-generated method stub
		if(pLocation != null)
		this.mUserLocation = pLocation;
	}

	@Override
	public void onLocationLost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationProviderDisabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationProviderStatusChanged(
			LocationProviderStatus pLocationProviderStatus, Bundle pBundle) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		final LocationSensorOptions locationSensorOptions = new LocationSensorOptions();
		locationSensorOptions.setAccuracy(Criteria.ACCURACY_COARSE);
		locationSensorOptions.setMinimumTriggerTime(1);
		locationSensorOptions.setMinimumTriggerDistance(1);
		this.enableLocationSensor(this, locationSensorOptions);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.mEngine.disableOrientationSensor(this);
		this.mEngine.disableLocationSensor(this);
	}
	
	private double getVelocity(){
		double result = 0;
		result = Math.acos(Math.sin(latitude_start) * Math.sin(latitude_finish) + Math.cos(latitude_start) * Math.cos(latitude_finish) * Math.cos(longitude_finish - longitude_start)) * RADIUS_EARTH_METERS;
//		result += pedo;
//		result /= 10;
		return result;
	}
}
