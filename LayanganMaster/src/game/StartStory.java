package game;

import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
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
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;

import android.content.Intent;
import android.view.Display;
import android.view.KeyEvent;

public class StartStory extends SimpleBaseGameActivity {

	private ITextureRegion mBackgroundTextureRegion, mGirl, mBoy1, mBoy2, mLove, mKuda, mBoy3, mIstana, mKuda2, mPenjaga, mBoy4, mBoy5, mLamp, mAwan, mLayangan, mSurat, mKirim;
	int cameraWidth;
	int cameraHeight;
	final Scene scene = new Scene();
	private BitmapTextureAtlas mBitmapTextureAtlas;
	Sprite background, girl, boy1, boy2, love, kuda, boy3, kuda2, istana, penjaga, boy4, boy5, lamp, awan, layangan, surat, kirim;
	Music music;
	float scele;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();
		scele = cameraWidth/400f;
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
				.createFromAsset(this.mBitmapTextureAtlas, this, "sc1.png", 0,
						0);
		try {
			
			ITexture sc2 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc2.png");
						}
					});
			ITexture sc3 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc3.png");
						}
					});
			ITexture sc4 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc4.png");
						}
					});
			ITexture sc5 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc5.png");
						}
					});
			ITexture sc6 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc6.png");
						}
					});
			ITexture sc7 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc7.png");
						}
					});
			ITexture sc8 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc8.png");
						}
					});
			ITexture sc9 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc9.png");
						}
					});
			ITexture sc10 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc10.png");
						}
					});
			ITexture sc11 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc11.png");
						}
					});
			ITexture sc12 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc12.png");
						}
					});
			ITexture sc13 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc13.png");
						}
					});
			ITexture sc14 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc14.png");
						}
					});
			ITexture sc15 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc15.png");
						}
					});
			ITexture sc16 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc16.png");
						}
					});
			ITexture sc17 = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("story/sc17.png");
						}
					});
			
			sc2.load();
			sc3.load();
			sc4.load();
			sc5.load();
			sc6.load();
			sc7.load();
			sc8.load();
			sc9.load();
			sc10.load();
			sc11.load();
			sc12.load();
			sc13.load();
			sc14.load();
			sc15.load();
			sc16.load();
			sc17.load();
			
			mGirl = TextureRegionFactory.extractFromTexture(sc2);
			mBoy1 = TextureRegionFactory.extractFromTexture(sc3);
			mBoy2 = TextureRegionFactory.extractFromTexture(sc4);
			mLove = TextureRegionFactory.extractFromTexture(sc5);
			mKuda = TextureRegionFactory.extractFromTexture(sc6);
			mBoy3 = TextureRegionFactory.extractFromTexture(sc7);
			mIstana = TextureRegionFactory.extractFromTexture(sc8);
			mKuda2 = TextureRegionFactory.extractFromTexture(sc9);
			mPenjaga = TextureRegionFactory.extractFromTexture(sc10);
			mBoy4 = TextureRegionFactory.extractFromTexture(sc11);
			mBoy5 = TextureRegionFactory.extractFromTexture(sc12);
			mLamp = TextureRegionFactory.extractFromTexture(sc13);
			mAwan = TextureRegionFactory.extractFromTexture(sc14);
			mLayangan = TextureRegionFactory.extractFromTexture(sc15);
			mSurat = TextureRegionFactory.extractFromTexture(sc16);
			mKirim = TextureRegionFactory.extractFromTexture(sc17);
		
			
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
		background.setScale(scele);
		scene.attachChild(background);		

		mEngine.registerUpdateHandler(new TimerHandler(2f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						if (i == 0) {
							music.play();
							girl = new Sprite(centerX, centerY, mGirl,
									getVertexBufferObjectManager());
							girl.setScale(scele);
							scene.attachChild(girl);
							i++;
						}else if( i == 1){
							boy1 = new Sprite(centerX, centerY, mBoy1,
									getVertexBufferObjectManager());
							boy1.setScale(scele);
							scene.attachChild(boy1);
							i++;
						}else if(i == 2){
							boy2 = new Sprite(centerX, centerY, mBoy2,
									getVertexBufferObjectManager());
							boy2.setScale(scele);
							scene.detachChild(boy1);
							scene.attachChild(boy2);
							i++;
						}else if(i==3){
							love = new Sprite(centerX, centerY, mLove,
									getVertexBufferObjectManager());
							love.setScale(scele);
							scene.attachChild(love);
							i++;
						}else if(i==4){
							i++;
							
							float paddingY = 0;
							float scaleCeil = (float) Math.ceil(scele);
							if(scele-scaleCeil != 0) paddingY = (cameraHeight*scaleCeil-cameraHeight*scele)/2; 	
							else paddingY = 0;
							
							kuda = new Sprite(-100, paddingY, mKuda,
									getVertexBufferObjectManager());
							kuda.setScaleCenter(0, 0);
							kuda.setScale(scele);							
							scene.attachChild(kuda);
							MoveXModifier lari = new MoveXModifier(4, -100, cameraWidth/3);
							lari.addModifierListener(new IModifierListener<IEntity>() {
								
								@Override
								public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
									// TODO Auto-generated method stub
									i++;
								}
							});
							kuda.registerEntityModifier(lari);
							
						}else if(i==6){
							scene.detachChild(girl);
							i++;
						}else if(i==7){
							i++;
							MoveXModifier pergi = new MoveXModifier(3, cameraWidth/3, cameraWidth);
							pergi.addModifierListener(new IModifierListener<IEntity>() {
								
								@Override
								public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
									// TODO Auto-generated method stub
									i++;
								}
							});
							kuda.registerEntityModifier(pergi);
							
						}else if(i==9){
							boy3 = new Sprite(centerX, centerY, mBoy3,
									getVertexBufferObjectManager());
							boy3.setScale(scele);
							scene.detachChild(boy2);
							scene.detachChild(love);
							scene.attachChild(boy3);
							i++;
						}else if(i==10){
							istana = new Sprite(centerX, centerY, mIstana,
									getVertexBufferObjectManager());
							istana.setScale(scele);
							scene.detachChild(boy3);
							scene.detachChild(background);
							scene.attachChild(istana);
							i++;
						}else if(i==11){
							i++;
							kuda2 = new Sprite(cameraWidth, centerY, mKuda2,
									getVertexBufferObjectManager());
							kuda2.setScale(scele);
							scene.attachChild(kuda2);
							MoveXModifier lari2 = new MoveXModifier(6, cameraWidth, cameraWidth/4);
							lari2.addModifierListener(new IModifierListener<IEntity>() {
								
								@Override
								public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
									// TODO Auto-generated method stub
									i++;
								}
							});
							kuda2.registerEntityModifier(lari2);
						}else if(i==13){
							scene.detachChild(kuda2);
							i++;
						}else if(i==14){
							penjaga = new Sprite(centerX, centerY, mPenjaga,
									getVertexBufferObjectManager());
							penjaga.setScale(scele);
							scene.detachChild(istana);
							scene.attachChild(penjaga);
							i++;
						}else if(i==15){
							scene.detachChild(penjaga);
							scene.attachChild(background);
							boy4 = new Sprite(centerX, centerY, mBoy4,
									getVertexBufferObjectManager());
							boy4.setScale(scele);
							scene.attachChild(boy4);
							i++;
						}else if(i==16){
							boy5 = new Sprite(centerX, centerY, mBoy5,
									getVertexBufferObjectManager());
							boy5.setScale(scele);
							scene.attachChild(boy5);
							lamp = new Sprite(centerX, centerY, mLamp,
									getVertexBufferObjectManager());
							lamp.setScale(scele);
							scene.attachChild(lamp);
							i++;
						}else if(i==17){
							awan = new Sprite(centerX, centerY, mAwan,
									getVertexBufferObjectManager());
							awan.setScale(scele);
							scene.detachChild(lamp);
							scene.attachChild(awan);
							
							i++;
						}else if(i==18){
							layangan = new Sprite(centerX, centerY, mLayangan,
									getVertexBufferObjectManager());	
							layangan.setScale(scele);
							scene.attachChild(layangan);
							i++;
						}else if(i==19){
							surat = new Sprite(centerX, centerY, mSurat,
									getVertexBufferObjectManager());
							surat.setScale(scele);
							scene.attachChild(surat);
							i++;
						}else if(i==20){
							kirim = new Sprite(centerX, centerY, mKirim,
									getVertexBufferObjectManager());
							kirim.setScale(scele);
							scene.detachChild(layangan);
							scene.detachChild(surat);
							scene.attachChild(kirim);
							i++;
						}else if(i==21){
							mEngine.unregisterUpdateHandler(pTimerHandler);
							music.stop();
							mEngine.unregisterUpdateHandler(pTimerHandler);
							startActivity(new Intent(getApplicationContext(),
									Menu.class));
							finish();
						}
					}
				}));

		return scene;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	    	music.stop();
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
