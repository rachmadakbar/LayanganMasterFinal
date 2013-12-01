package game;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
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

import android.view.Display;




public class BattleMode extends SimpleBaseGameActivity {
	
	private ITextureRegion mBackgroundTextureRegion, mHitArea, mCollidePoint;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, hitArea, collidePoint, buttonUp, buttonDown;
	private Font mFont;
	private Text attackCountText;
	private int attackCount = 0;
	
	private ITexture mTextureArrowUp, mTextureArrowDown, mTextureArrowUpButton, mTextureArrowDownButton;
	private ITextureRegion mArrowUpTextureRegion, mArrowDownTextureRegion, mArrowUpButtonTextureRegion, mArrowDownButtonTextureRegion;
	private ArrayList<Sprite> arrowList = new ArrayList<Sprite>();
	private ArrayList<Sprite> arrowsToBeAdded = new ArrayList<Sprite>();
	private ArrayList<String> arrowTypes = new ArrayList<String>();
	private Camera camera;
	private String currentArrowType = "none";
	private float startingDuration = 8.0f;
	
	float rachmad = 0.8f;

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();
		camera = new Camera(0, 0, cameraWidth, cameraHeight);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						cameraWidth, cameraHeight), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// setting assets path for easy access
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		// loading the image inside the container
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "sky.jpg",
						0, 0);
		
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		FontFactory.setAssetBasePath("font/");

		mFont = FontFactory.createFromAsset(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
				this.getAssets(), "RAVIE.TTF", 40f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mFont.load();
		
		try {
			
			ITexture targetmark_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/targetmark.png");
						}
					});
			ITexture collidepoint_asset = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("asset/collidepoint.png");
						}
					});

			targetmark_asset.load();
			collidepoint_asset.load();

			mHitArea = TextureRegionFactory.extractFromTexture(targetmark_asset);
			mCollidePoint = TextureRegionFactory.extractFromTexture(collidepoint_asset);
			
			this.mTextureArrowUp = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("asset/panah up.png");
				}
			});
			
			this.mTextureArrowDown = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("asset/panah down.png");
				}
			});
			
			this.mTextureArrowUpButton = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("asset/panah up.png");
				}
			});
			
			this.mTextureArrowDownButton = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("asset/panah down.png");
				}
			});
			
			this.mTextureArrowUp.load();
			this.mTextureArrowDown.load();
			this.mTextureArrowUpButton.load();
			this.mTextureArrowDownButton.load();
			this.mArrowUpTextureRegion = TextureRegionFactory.extractFromTexture(this.mTextureArrowUp);
			this.mArrowDownTextureRegion = TextureRegionFactory.extractFromTexture(this.mTextureArrowDown);
			this.mArrowUpButtonTextureRegion = TextureRegionFactory.extractFromTexture(this.mTextureArrowUpButton);
			this.mArrowDownButtonTextureRegion = TextureRegionFactory.extractFromTexture(this.mTextureArrowDownButton);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected Scene onCreateScene() {
		final int centerX = (int) ((cameraWidth - mBackgroundTextureRegion
				.getWidth()) / 2);
		final int centerY = (int) ((cameraHeight - mBackgroundTextureRegion
				.getHeight()) / 2);

		background = new Sprite(centerX, centerY, mBackgroundTextureRegion,
				getVertexBufferObjectManager());
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		/*this.mEngine.registerUpdateHandler(new IUpdateHandler() {
			public void onUpdate(float pSecondsElapsed) {
				updateSpritePosition();
			}

			public void reset() {
				// TODO Auto-generated method stub
			}
		});*/
		
		background.setScale(Menu.player.scale);
		scene.attachChild(background);

		hitArea = new Sprite(0, 0, mHitArea, getVertexBufferObjectManager());


		/*if (Menu.player.scale != rachmad) {
			hitArea.setScaleCenter(0, 0);
			hitArea.setScale(Menu.player.scale);
		}*/

		hitArea.setPosition(cameraWidth*0.5f - (hitArea.getWidth() / 2), Menu.player.getCameraHeight(0.25) - (hitArea.getHeight() / 2));
		scene.attachChild(hitArea);
		scene.registerTouchArea(hitArea);
		
		if (Menu.player.scale != rachmad) {
			collidePoint = new Sprite(0, 0, mCollidePoint, getVertexBufferObjectManager());
			collidePoint.setScaleCenter(0, 0);
			collidePoint.setScale(Menu.player.scale);
		} else {
			collidePoint = new Sprite(0, Menu.player.paddingY, mCollidePoint,
					getVertexBufferObjectManager());
		}
		collidePoint.setPosition(cameraWidth*0.5f + (hitArea.getWidth()), Menu.player.getCameraHeight(0.5) - collidePoint.getHeight());
		scene.attachChild(collidePoint);
		
		attackCountText = new Text(cameraWidth / 2, 0,
				mFont, "               ", getVertexBufferObjectManager());
		attackCountText.setScaleCenter(0, 0);
		attackCountText.setScale(Menu.player.scale);
		scene.attachChild(attackCountText);
		
		this.createSpriteSpawnTimeHandler();
		
		scene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float pSecondsElapsed) {
				Iterator<Sprite> targets = arrowList.iterator();
				Iterator<String> types = arrowTypes.iterator();
				Sprite _target;
				String _type;
				boolean hit = false;
				int count = 0;
				// iterating over the targets
				while (targets.hasNext() && types.hasNext()) {
					_target = targets.next();
					_type = types.next();
				
					// if the targets collides with a projectile, remove the
					// projectile and set the hit flag to true
					if (_target.collidesWith(hitArea)) {
						//removeSprite(_target, targets);
						currentArrowType = _type; 
						//break;
					}  else {
						count++;
						if(count == arrowList.size()) {
							currentArrowType = "none";
						}
					}
					if(_target.getX() > cameraWidth) {
						removeSprite(_target, targets, types);
					}
					// if a projectile hit the target, remove the target, increment
					// the hit count, and update the score
					//if (hit) {
						
						//removeSprite(_target, targets);
						//hit = false;
						//hitCount++;
						//hitCountText.setText("Score: "+String.valueOf(hitCount));
						//Log.d("skor", "hitcount "+hitCount);
						
					//}
					
				}
				
				
				
				//if(hitArea.collidesWith(pOtherShape))
				
				arrowList.addAll(arrowsToBeAdded);
				arrowsToBeAdded.clear();
				
				
			}
		});
		
		buttonUp = new Sprite(0, this.cameraHeight - mArrowUpButtonTextureRegion.getHeight()-10, this.mArrowUpButtonTextureRegion, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					
					// cek kl collide point collision sama active arrow
					if(currentArrowType.equals("up")) {
						attackCount++;
						attackCountText.setText(""+attackCount );
					} else {
						attackCount--;
						attackCountText.setText(""+attackCount );
					}
					/*isSantai = true;
					changeScene();
					scene.attachChild(count);
					scene.registerTouchArea(start);
					scene.attachChild(start);*/
					/*Debug.e("kite", "kite1");
					Menu.player.setKite(1);
					startActivity(new Intent(getApplicationContext(),
							Test.class));*/
					//finish();
					
				}
				return true;
			};
		};

		buttonUp.setPosition(0, this.cameraHeight - mArrowUpButtonTextureRegion.getHeight());
		if (Menu.player.scale != rachmad) {
			buttonUp.setScaleCenter(0, 0);
			buttonUp.setScale(Menu.player.scale);
		}

		
		scene.attachChild(buttonUp);
		scene.registerTouchArea(buttonUp);
		
		buttonDown = new Sprite(cameraWidth - (mArrowDownButtonTextureRegion.getWidth()), cameraHeight - mArrowDownButtonTextureRegion.getHeight()-10, this.mArrowDownButtonTextureRegion, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X,
					float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					
					// cek kl collide point collision sama active arrow
					if(currentArrowType.equals("down")) {
						attackCount++;
						attackCountText.setText(""+attackCount);
					} else {
						attackCount--;
						attackCountText.setText(""+attackCount);
					}
					
					
				}
				return true;
			};
		};


		

		if (Menu.player.scale != rachmad) {
			buttonDown.setScaleCenter(0, 0);
			buttonDown.setScale(Menu.player.scale);
		}
		
		buttonDown.setPosition(cameraWidth - (mArrowDownButtonTextureRegion.getWidth()), cameraHeight - (buttonDown.getHeight()*2));
		scene.attachChild(buttonDown);
		scene.registerTouchArea(buttonDown);
		
		
		
		return scene;
	}

	
	/* safely detach the sprite from the scene and remove it from the iterator */
	public void removeSprite(final Sprite _sprite, Iterator<Sprite> it, Iterator<String> type) {
		runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				scene.detachChild(_sprite);
			}
		});
		it.remove();
		type.remove();
	}
	
	public void addArrow() {
	    Random rand = new Random();
	 
	    float y =  Menu.player.getCameraHeight(0.25) - (mArrowUpTextureRegion.getHeight() / 2);
	    //float minX = mArrowUpTextureRegion.getWidth();
	    //float maxX =  camera.getWidth() - mArrowUpTextureRegion.getWidth();
	    //float rangeX = maxX - minX;
	    float x = 0;
	    
	    Sprite arrow = new Sprite(x, y, this.mArrowUpTextureRegion.deepCopy(), this.getVertexBufferObjectManager());
	    
	    float temp = rand.nextFloat();
	    String type = "up";
	    
	    if (temp < 0.25) {
	    	type = "down";
	    	arrow = new Sprite(x, y, this.mArrowDownTextureRegion.deepCopy(), this.getVertexBufferObjectManager());
	    } else if (temp < 0.5) {
	    	type = "up";
	    	arrow = new Sprite(x, y, this.mArrowUpTextureRegion.deepCopy(), this.getVertexBufferObjectManager());
	    } else if (temp < 0.75) {
	    	type = "down";
	    	arrow = new Sprite(x, y, this.mArrowDownTextureRegion.deepCopy(), this.getVertexBufferObjectManager());
	    }
	    
	    /*if (Menu.player.scale != rachmad) {
			arrow.setScaleCenter(0, 0);
			arrow.setScale(Menu.player.scale);
		}*/
	    
	    
	    this.scene.attachChild(arrow);
	 
	    int minDuration = 2;
	    int maxDuration = 4;
	    int rangeDuration = maxDuration - minDuration;
	    float actualDuration = startingDuration;
	 
	    MoveXModifier mod = new MoveXModifier(actualDuration,-arrow.getWidth(),
	    		 cameraWidth);
	    if((arrowsToBeAdded.size() % 10) == 0 && startingDuration > 2) {
	    	startingDuration = startingDuration - 0.2f;
	    }
	    arrow.registerEntityModifier(mod.deepCopy());
	    
	 
	    this.arrowsToBeAdded.add(arrow);
	    this.arrowTypes.add(type);
	 
	}
	
	private void createSpriteSpawnTimeHandler() {
	    TimerHandler spriteTimerHandler;
	    float mEffectSpawnDelay = 1f;
	 
	    spriteTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
	    new ITimerCallback() {
	 
	        @Override
	        public void onTimePassed(TimerHandler pTimerHandler) {
	            addArrow();
	        }
	    });
	 
	    getEngine().registerUpdateHandler(spriteTimerHandler);
	}
	
}
