package com.htetznaing.smsunlockernew;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class about extends Activity implements B4AActivity{
	public static about mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.smsunlockernew", "com.htetznaing.smsunlockernew.about");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (about).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.htetznaing.smsunlockernew", "com.htetznaing.smsunlockernew.about");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.smsunlockernew.about", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (about) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (about) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return about.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (about) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (about) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.StringUtils _su = null;
public anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lstone = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _adview1 = null;
public com.htetznaing.smsunlockernew.main _main = null;
public com.htetznaing.smsunlockernew.smsunlocker _smsunlocker = null;
public com.htetznaing.smsunlockernew.ads _ads = null;
public com.htetznaing.smsunlockernew.s1 _s1 = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imvlogo = null;
anywheresoftware.b4a.objects.LabelWrapper _lblname = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _c = null;
int _height = 0;
 //BA.debugLineNum = 21;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 24;BA.debugLine="Activity.Title = \"About\"";
mostCurrent._activity.setTitle((Object)("About"));
 //BA.debugLineNum = 25;BA.debugLine="Activity.Color = Colors.RGB (235,235,235)";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (235),(int) (235),(int) (235)));
 //BA.debugLineNum = 27;BA.debugLine="Dim imvLogo As ImageView";
_imvlogo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 28;BA.debugLine="imvLogo.Initialize (\"\")";
_imvlogo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 29;BA.debugLine="imvLogo.Bitmap = LoadBitmap(File.DirAssets , \"log";
_imvlogo.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"logo.png").getObject()));
 //BA.debugLineNum = 30;BA.debugLine="imvLogo.Gravity = Gravity.FILL";
_imvlogo.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 31;BA.debugLine="Activity.AddView ( imvLogo , 50%x - 50dip  , 20di";
mostCurrent._activity.AddView((android.view.View)(_imvlogo.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 33;BA.debugLine="Dim lblName As  Label";
_lblname = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 35;BA.debugLine="bg.Initialize (Colors.DarkGray , 10dip)";
_bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.DarkGray,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 36;BA.debugLine="lblName.Initialize (\"\")";
_lblname.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 37;BA.debugLine="lblName.Background = bg";
_lblname.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 38;BA.debugLine="lblName.Gravity = Gravity.CENTER";
_lblname.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 39;BA.debugLine="lblName.Text = \"SMS Unlocker\"";
_lblname.setText((Object)("SMS Unlocker"));
 //BA.debugLineNum = 40;BA.debugLine="lblName.TextSize = 13";
_lblname.setTextSize((float) (13));
 //BA.debugLineNum = 41;BA.debugLine="lblName.TextColor = Colors.White";
_lblname.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 42;BA.debugLine="Activity.AddView (lblName , 100%x / 2 - 90dip , 1";
mostCurrent._activity.AddView((android.view.View)(_lblname.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (130)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (180)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 43;BA.debugLine="lblName.Height = su.MeasureMultilineTextHeight (l";
_lblname.setHeight((int) (mostCurrent._su.MeasureMultilineTextHeight((android.widget.TextView)(_lblname.getObject()),_lblname.getText())+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 46;BA.debugLine="Dim c As ColorDrawable";
_c = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 47;BA.debugLine="c.Initialize (Colors.White , 10dip )";
_c.Initialize(anywheresoftware.b4a.keywords.Common.Colors.White,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 48;BA.debugLine="lstOne.Initialize (\"lstOnes\")";
mostCurrent._lstone.Initialize(mostCurrent.activityBA,"lstOnes");
 //BA.debugLineNum = 49;BA.debugLine="lstOne.Background = c";
mostCurrent._lstone.setBackground((android.graphics.drawable.Drawable)(_c.getObject()));
 //BA.debugLineNum = 50;BA.debugLine="lstOne.SingleLineLayout .Label.TextSize = 12";
mostCurrent._lstone.getSingleLineLayout().Label.setTextSize((float) (12));
 //BA.debugLineNum = 51;BA.debugLine="lstOne.SingleLineLayout .Label .TextColor = Color";
mostCurrent._lstone.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 52;BA.debugLine="lstOne.SingleLineLayout .Label .Gravity = Gravity";
mostCurrent._lstone.getSingleLineLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 53;BA.debugLine="lstOne.SingleLineLayout .ItemHeight = 40dip";
mostCurrent._lstone.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 54;BA.debugLine="lstOne.AddSingleLine2 (\"Developed By : Khun Htetz";
mostCurrent._lstone.AddSingleLine2("Developed By : Khun Htetz Naing    ",(Object)(1));
 //BA.debugLineNum = 55;BA.debugLine="lstOne.AddSingleLine2 (\"Powered By : Myanmar Andr";
mostCurrent._lstone.AddSingleLine2("Powered By : Myanmar Android Apps    ",(Object)(2));
 //BA.debugLineNum = 56;BA.debugLine="lstOne.AddSingleLine2 (\"Facebook : www.facebook.c";
mostCurrent._lstone.AddSingleLine2("Facebook : www.facebook.com/MmFreeAndroidApps  ",(Object)(3));
 //BA.debugLineNum = 57;BA.debugLine="lstOne.AddSingleLine2 (\"Website : www.HtetzNaing.";
mostCurrent._lstone.AddSingleLine2("Website : www.HtetzNaing.com",(Object)(4));
 //BA.debugLineNum = 58;BA.debugLine="Activity.AddView ( lstOne, 30dip , 170dip , 100%x";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lstone.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (162)));
 //BA.debugLineNum = 60;BA.debugLine="AdView1.Initialize2(\"AdView1\",\"ca-app-pub-4173348";
mostCurrent._adview1.Initialize2(mostCurrent.activityBA,"AdView1","ca-app-pub-4173348573252986/4080989757",mostCurrent._adview1.SIZE_SMART_BANNER);
 //BA.debugLineNum = 61;BA.debugLine="Dim height As Int";
_height = 0;
 //BA.debugLineNum = 62;BA.debugLine="If GetDeviceLayoutValues.ApproximateScreenSize <";
if (anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(mostCurrent.activityBA).getApproximateScreenSize()<6) { 
 //BA.debugLineNum = 64;BA.debugLine="If 100%x > 100%y Then height = 32dip Else height";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)) { 
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32));}
else {
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));};
 }else {
 //BA.debugLineNum = 67;BA.debugLine="height = 90dip";
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90));
 };
 //BA.debugLineNum = 69;BA.debugLine="Activity.AddView(AdView1, 0dip, 100%y - height, 1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._adview1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-_height),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_height);
 //BA.debugLineNum = 70;BA.debugLine="AdView1.LoadAd";
mostCurrent._adview1.LoadAd();
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 94;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 90;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim su As StringUtils";
mostCurrent._su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 16;BA.debugLine="Dim p As PhoneIntents";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 17;BA.debugLine="Dim lstOne As ListView";
mostCurrent._lstone = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim AdView1 as AdView";
mostCurrent._adview1 = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _lblcredit_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _facebook = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 73;BA.debugLine="Sub lblCredit_Click";
 //BA.debugLineNum = 74;BA.debugLine="Try";
try { //BA.debugLineNum = 76;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 78;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb://";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 79;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 83;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 84;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.facebook.";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 86;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
return "";
}
public static String  _lstones_itemclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _facebook = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 98;BA.debugLine="Sub lstOnes_ItemClick (Position As Int, Value As O";
 //BA.debugLineNum = 99;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)(1),(Object)(2),(Object)(3),(Object)(4))) {
case 0: {
 //BA.debugLineNum = 101;BA.debugLine="Try";
try { //BA.debugLineNum = 103;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 105;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://profile/100006126339714");
 //BA.debugLineNum = 106;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 110;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 111;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.faceboo";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MgHtetzNaing");
 //BA.debugLineNum = 113;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 break; }
case 1: {
 //BA.debugLineNum = 117;BA.debugLine="Try";
try { //BA.debugLineNum = 119;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 121;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 122;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e18) {
			processBA.setLastException(e18); //BA.debugLineNum = 126;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 127;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.faceboo";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 129;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 break; }
case 2: {
 //BA.debugLineNum = 133;BA.debugLine="Try";
try { //BA.debugLineNum = 135;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 137;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 138;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e28) {
			processBA.setLastException(e28); //BA.debugLineNum = 142;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 143;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.faceboo";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 145;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 break; }
case 3: {
 //BA.debugLineNum = 149;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.htetzn";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("http://www.htetznaing.com")));
 break; }
}
;
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
}
