package game;



import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.sensor.location.ILocationListener;
import org.andengine.input.sensor.location.LocationProviderStatus;
import org.andengine.input.sensor.location.LocationSensorOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

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

public class PilihMode extends SimpleBaseGameActivity implements SensorEventListener, ILocationListener {

	
	private ITextureRegion mBackgroundTextureRegion, mHome, mMenu, mSantaiButton, mEnerjikButton;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, home, menu, santaiButton, enerjikButton;
	boolean isSantai = true;
	
	float rachmad = 0.8f;
	
	private ITextureRegion mStart, mFinish;
	Sprite finish, start;
	private Font mFont;
	private Text count, lat1,lat2,long1,long2, direction;
	private int tempX = 0;
	private int tempY = 0;
	private int accellerometerSpeedX;
	private int accellerometerSpeedY;
	private int pedo;
	private SensorManager sensorManager;
	private Location mUserLocation;
	private float latitude_start, longitude_start;
	private float latitude_finish, longitude_finish;
	boolean counterON = false;
	private static final double RADIUS_EARTH_METERS = 6371000;
	int ii = 10;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
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
		
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		FontFactory.setAssetBasePath("font/");

		mFont = FontFactory.createFromAsset(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				this.getAssets(), "RAVIE.TTF", 40f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mFont.load();
		
		
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
							return getAssets().open("asset/pilih_mode.png");
						}
					});

			ITexture asset_santaibutton = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/santai.png");
						}
					});

			ITexture asset_enerjikbutton = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/enerjik.png");
						}
					});


			menu_asset.load();
			home_asset.load();
			asset_santaibutton.load();
			asset_enerjikbutton.load();
						
			mHome = TextureRegionFactory.extractFromTexture(home_asset);
			mMenu = TextureRegionFactory.extractFromTexture(menu_asset);
			mSantaiButton = TextureRegionFactory.extractFromTexture(asset_santaibutton);
			mEnerjikButton = TextureRegionFactory.extractFromTexture(asset_enerjikbutton);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		
		
	}

	@Override
	protected Scene onCreateScene() {
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
		
		start = new Sprite(0, 0, mStart, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					if(!isSantai) {
						for(int i = 0 ; i<10; i++){
							mUserLocation.getLatitude();
							mUserLocation.getLongitude();
						}
						latitude_start = (float)mUserLocation.getLatitude();
						longitude_start = (float) mUserLocation.getLongitude();
					}
					counterON= true;
					mEngine.registerUpdateHandler(new TimerHandler(1f, true,
							new ITimerCallback() {
								@Override
								public void onTimePassed(TimerHandler pTimerHandler) {
									if (ii >= 0) {
										// scene.detachChild(start);
										scene.unregisterTouchArea(start);
										count.setText("" + ii--);
									} else {
										counterON = false;
										if(!isSantai) {
											latitude_finish = (float)mUserLocation.getLatitude();
											longitude_finish = (float) mUserLocation.getLongitude();
										}
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


		
		
		finish = new Sprite(0, 0, mFinish, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					/*if(!isSantai) {
						lat1.setText(latitude_start+"");
						lat2.setText(latitude_finish+"");
						long1.setText(longitude_start+"");
						long2.setText(longitude_finish+"");
						//count.setText("v="+String.format("%.3f", getVelocity()));
						Debug.e("velo", latitude_start+" "+latitude_finish + " " + longitude_start+" "+longitude_finish);
						
					}*/	
					startActivity(new Intent(getApplicationContext(),
							BattleMode.class));
					finish();
				}
				return true;
			};
		};

		if (Menu.player.scale != 0.8f) {
			finish.setScaleCenter(0, 0);
			finish.setScale(Menu.player.scale);
		}

		finish.setPosition(cameraWidth / 2, Menu.player.getCameraHeight(0.7));
		
		
		count = new Text(cameraWidth / 2, Menu.player.getCameraHeight(0.5),
				mFont, "               ", getVertexBufferObjectManager());
		count.setScaleCenter(0, 0);
		count.setScale(Menu.player.scale);
		
		direction = new Text(0, 0, mFont, "               ", getVertexBufferObjectManager());
		direction.setScaleCenter(0, 0);
		direction.setScale(Menu.player.scale);
		
		
		lat1 = new Text(cameraWidth*0.05f, Menu.player.getCameraHeight(0.2),
				mFont, "                                                                                          ", getVertexBufferObjectManager());
		lat1.setScaleCenter(0, 0);
		lat1.setScale(Menu.player.scale);
		
		
		lat2 = new Text(cameraWidth*0.05f, Menu.player.getCameraHeight(0.4),
				mFont, "                                                                                          ", getVertexBufferObjectManager());
		lat2.setScaleCenter(0, 0);
		lat2.setScale(Menu.player.scale);
		
		
		long1 = new Text(cameraWidth*0.5f, Menu.player.getCameraHeight(0.2),
				mFont, "                                                                                          ", getVertexBufferObjectManager());
		long1.setScaleCenter(0, 0);
		long1.setScale(Menu.player.scale);
		
		
		
		long2 = new Text(cameraWidth*0.5f, Menu.player.getCameraHeight(0.4),
				mFont, "                                                                                          ", getVertexBufferObjectManager());
		long2.setScaleCenter(0, 0);
		long2.setScale(Menu.player.scale);
		
		
		santaiButton = new Sprite(0, 0, mSantaiButton, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					isSantai = true;
					changeScene();
					scene.attachChild(count);
					scene.registerTouchArea(start);
					scene.attachChild(start);
					/*Debug.e("kite", "kite1");
					Menu.player.setKite(1);
					startActivity(new Intent(getApplicationContext(),
							Test.class));*/
					//finish();
					
				}
				return true;
			};
		};


		if (Menu.player.scale != rachmad) {
			santaiButton.setScaleCenter(0, 0);
			santaiButton.setScale(Menu.player.scale);
		}

		santaiButton.setPosition(cameraWidth*0.25f, Menu.player.getCameraHeight(0.4));
		scene.attachChild(santaiButton);
		scene.registerTouchArea(santaiButton);

		enerjikButton = new Sprite(0, 0, mEnerjikButton, getVertexBufferObjectManager()) {
			
			private String generateDirection() {
				String directionName = "utara";
				Random rand = new Random();
				float temp = rand.nextFloat();
				if(temp < 0.25) {
					directionName = "barat";
				} else if(temp < 0.5) {
					directionName = "timur";
				} else  if(temp < 0.25) {
					directionName = "selatan";
				}
				return directionName;
			}
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				String directionName = generateDirection();
				if (pSceneTouchEvent.isActionUp()) {
					isSantai = false;
					changeScene();
					scene.attachChild(count);
					scene.attachChild(lat1);
					scene.attachChild(lat2);
					scene.attachChild(long1);
					scene.attachChild(long2);
					scene.registerTouchArea(start);
					scene.attachChild(start);
					scene.attachChild(direction);
					direction.setText(""+directionName);
					/*Debug.e("kite", "kite2");
					Menu.player.setKite(2);
					startActivity(new Intent(getApplicationContext(),
							PilihMode.class));*/
					//finish();
				}
				return true;
			};
		};

		if (Menu.player.scale != rachmad) {
			enerjikButton.setScaleCenter(0, 0);
			enerjikButton.setScale(Menu.player.scale);
		}

		enerjikButton.setPosition(cameraWidth*0.25f, Menu.player.getCameraHeight(0.2));

		scene.attachChild(enerjikButton);
		scene.registerTouchArea(enerjikButton);
	
		return scene;
	}

	private void changeScene() {
		scene.unregisterTouchArea(santaiButton);
		scene.unregisterTouchArea(enerjikButton);
		mMenu = null;
		mSantaiButton = null;
		mEnerjikButton = null;
		scene.detachChild(santaiButton);
		scene.detachChild(enerjikButton);
		scene.detachChild(menu);
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

	@Override
	public void onLocationProviderEnabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location pLocation) {
		// TODO Auto-generated method stub
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
	protected void onResume() {
		super.onResume();

		

		final LocationSensorOptions locationSensorOptions = new LocationSensorOptions();
		locationSensorOptions.setAccuracy(Criteria.ACCURACY_COARSE);
		locationSensorOptions.setMinimumTriggerTime(0);
		locationSensorOptions.setMinimumTriggerDistance(0);
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
