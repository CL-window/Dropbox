package cn.kejin.learn;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private final static String TAG = "MainActivity";

    private Tracker mTracker = AnalyticsApplication.getDefaultTracker();

    private final static String BASE_URL = "http://getbootstrap.com";
    /**
     * http://v3.bootcss.com/       Bootstrap 中文文档 (Chinese)
     * https://kkbruce.tw/bs3/      Bootstrap 3 中文手冊 (Chinese (Traditional))
     * http://getbootstrap.dk/      Bootstrap på Dansk (Danish)
     * http://www.oneskyapp.com/fr/docs/bootstrap/getting-started/  Bootstrap en Français (French)
     * http://holdirbootstrap.de/   Bootstrap auf Deutsch (German)
     * http://www.hackerstribe.com/guide/IT-bootstrap-3.1.1/    Bootstrap in Italiano (Italian)
     * http://bootstrapk.com/       Bootstrap 한국어 (Korean)
     * http://bootstrapbrasil.github.io/    Bootstrap em Português do Brasil (Brazilian Portuguese)
     * http://www.oneskyapp.com/ru/docs/bootstrap/  Bootstrap по-русски (Russian)
     * http://www.oneskyapp.com/es/docs/bootstrap/  Bootstrap en Español (Spanish)
     * http://www.trbootstrap.com   Türkçe Bootstrap (Turkish)
     * http://twbs.docs.org.ua      Bootstrap українською (Ukrainian)
     * http://getbootstrap.com.vn   Bootstrap bằng tiếng Việt (Vietnamese)
     */
    private static HashMap<Integer, String> mWebsites= new HashMap<>();
    static {
        mWebsites.put(R.id.zh_cn, "http://v3.bootcss.com/");
        mWebsites.put(R.id.zh_tr, "https://kkbruce.tw/bs3/");
        mWebsites.put(R.id.en, "http://getbootstrap.com/");
        mWebsites.put(R.id.da, "http://getbootstrap.dk/");
        mWebsites.put(R.id.fr, "http://www.oneskyapp.com/fr/docs/bootstrap/");
        mWebsites.put(R.id.ge, "http://holdirbootstrap.de/");
        mWebsites.put(R.id.it, "http://www.hackerstribe.com/guide/IT-bootstrap-3.1.1/");
        mWebsites.put(R.id.ko, "http://bootstrapk.com/");
        mWebsites.put(R.id.br, "http://bootstrapbrasil.github.io/");
        mWebsites.put(R.id.ru, "http://www.oneskyapp.com/ru/docs/bootstrap/");
        mWebsites.put(R.id.sp, "http://www.oneskyapp.com/es/docs/bootstrap/");
        mWebsites.put(R.id.tu, "http://www.trbootstrap.com/");
        mWebsites.put(R.id.uk, "http://twbs.docs.org.ua/");
        mWebsites.put(R.id.vi, "http://getbootstrap.com.vn/");
    }

    private static HashMap<Integer, String> mAssetsDirs = new HashMap<>();
    static {
        mAssetsDirs.put(R.id.zh_cn, "zh-cn");
        mAssetsDirs.put(R.id.zh_tr, "zh-tr");
        mAssetsDirs.put(R.id.en, "en");
        mAssetsDirs.put(R.id.da, "da");
        mAssetsDirs.put(R.id.fr, "fr");
        mAssetsDirs.put(R.id.ge, "ge");
        mAssetsDirs.put(R.id.it, "it");
        mAssetsDirs.put(R.id.ko, "ko");
        mAssetsDirs.put(R.id.br, "br");
        mAssetsDirs.put(R.id.ru, "ru");
        mAssetsDirs.put(R.id.sp, "sp");
        mAssetsDirs.put(R.id.tu, "tu");
        mAssetsDirs.put(R.id.uk, "uk");
        mAssetsDirs.put(R.id.vi, "vi");
    }

    private static HashMap<String, Integer> mLangIds = new HashMap<>();
    static {
        mLangIds.put("zh", R.id.zh_cn);
        mLangIds.put("zh_TW", R.id.zh_tr);
        mLangIds.put("en", R.id.en);
        mLangIds.put("da", R.id.da);
        mLangIds.put("fr", R.id.fr);
        mLangIds.put("de", R.id.ge);
        mLangIds.put("it", R.id.it);
        mLangIds.put("ko", R.id.ko);
        mLangIds.put("pt", R.id.br);
        mLangIds.put("ru", R.id.ru);
        mLangIds.put("es", R.id.sp);
        mLangIds.put("tr", R.id.tu);
        mLangIds.put("uk", R.id.uk);
        mLangIds.put("vi", R.id.vi);
    }

    private static HashMap<Integer, PagesName> mPagePath = new HashMap<>();
    static {
        mPagePath.put(R.id.zh_cn, new PagesName());
        mPagePath.put(R.id.zh_tr, new PagesName("GettingStarted", "CSS", "Components", "JavaScript"));
        mPagePath.put(R.id.en, new PagesName());
        mPagePath.put(R.id.da, new PagesName());
        mPagePath.put(R.id.fr, new PagesName());
        mPagePath.put(R.id.ge, new PagesName());
        mPagePath.put(R.id.it, new PagesName());
        mPagePath.put(R.id.ko, new PagesName());
        mPagePath.put(R.id.br, new PagesName());
        mPagePath.put(R.id.ru, new PagesName());
        mPagePath.put(R.id.sp, new PagesName());
        mPagePath.put(R.id.tu, new PagesName());
        mPagePath.put(R.id.uk, new PagesName());
        mPagePath.put(R.id.vi, new PagesName());

    }

    private static class PagesName
    {
        public String mGettingStarted = "getting-started";
        public String mCss = "css";
        public String mComponents = "components";
        public String mJavaScript = "javascript";

        public PagesName()
        {
            //
        }

        public PagesName(String gs, String css, String comp, String js)
        {
            mGettingStarted = gs;
            mCss = css;
            mComponents = comp;
            mJavaScript = js;
        }

        public String getPageName(int itemId)
        {
            switch (itemId) {
                case R.id.getting_start:
                    return mGettingStarted;
                case R.id.css:
                    return mCss;
                case R.id.components:
                    return mComponents;
                case R.id.javascript:
                    return mJavaScript;
            }
            return "";
        }
    }

    private DrawerLayout mDrawerLayout = null;

    private WebView mWebView = null;

    private AlertDialog mProgressDialog = null;

    private NavigationView mNavigationView = null;

    private String mCurrentUrl = "";
    private MenuItem mCurLangItem = null;
    private MenuItem mCurPageItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupAdmobView();
        setupContentView();
        setupWebView();

        setupApplication();

        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void loadPage()
    {
        int pageId = mCurPageItem.getItemId();
        int langId = mCurLangItem.getItemId();
        PagesName pagesName = mPagePath.get(langId);
        String url = mWebsites.get(langId)+ pagesName.getPageName(pageId);

        mCurrentUrl = url;
        mWebView.loadUrl(url);
    }

    private void setupApplication()
    {
        String lang = Locale.getDefault().getLanguage();

        int langId = mLangIds.get(lang);

        mCurLangItem = mNavigationView.getMenu().findItem(langId);
        if (mCurLangItem == null) {
            mCurLangItem = mNavigationView.getMenu().findItem(R.id.en);
        }
        mCurLangItem.setChecked(true);

        mCurPageItem = mNavigationView.getMenu().findItem(R.id.bootstrap);
        mCurPageItem.setChecked(true);

        mDrawerLayout.openDrawer(GravityCompat.START);
        mDrawerLayout.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 1000);
        loadPage();
    }

    protected void setupAdmobView()
    {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    protected void setupWebView()
    {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                super.onProgressChanged(view, newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url)
            {
                if (url.equals(mCurrentUrl) || url.equals(mCurrentUrl + "/")) {
                    view.loadUrl(url);
                }
                else {

                    Snackbar.make(view, url, Snackbar.LENGTH_LONG).setAction(getString(R.string.open), new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    }).show();
                }

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                mProgressDialog.show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                mProgressDialog.dismiss();
                super.onPageFinished(view, url);
            }
        });
    }

    protected void setupContentView()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(new ProgressBar(this));

        mProgressDialog = builder.create();
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mWebView = (WebView) findViewById(R.id.webView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);

        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private boolean mBackFlag = false;
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event)
    {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

            if (!mBackFlag) {
                mBackFlag = true;
                Snackbar.make(mDrawerLayout, getString(R.string.press_one_more), Snackbar.LENGTH_SHORT).show();
                mDrawerLayout.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mBackFlag = false;
                    }
                }, 2000);
                return true;
            }

            mBackFlag = false;
        }

        return super.onKeyDown(keycode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_about:
                View infoView = View.inflate(this, R.layout.layout_about_info, null);
                infoView.findViewById(R.id.github).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://github.com/liungkejin"));
                        startActivity(intent);
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(infoView);
                builder.setCancelable(true);
                builder.create().show();
                break;

            case R.id.action_share:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.action_share));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_info));
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem)
    {
        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.bootstrap:
            case R.id.getting_start:
            case R.id.css:
            case R.id.components:
            case R.id.javascript:
                if (mCurPageItem == menuItem) {
                    break;
                }
                mCurPageItem.setChecked(false);
                mCurPageItem = menuItem;
                mCurPageItem.setChecked(true);
                loadPage();
                break;

            default:
                if (mCurLangItem == menuItem) {
                    break;
                }
                mCurLangItem.setChecked(false);
                mCurLangItem = menuItem;
                mCurLangItem.setChecked(true);
                loadPage();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
