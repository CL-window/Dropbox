package cn.kejin.learn;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private final static String TAG = "MainActivity";

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
        mWebsites.put(R.id.fr, "http://www.oneskyapp.com/fr/docs/bootstrap/getting-started/");
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
        /**
         *
         zh
         en
         da
         fr
         de
         it
         ko
         pt
         ru
         es
         tr
         uk
         vi
         */
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

    private static HashMap<Integer, String> mPagePath = new HashMap<>();
    static {
        mPagePath.put(R.id.bootstrap, "");
        mPagePath.put(R.id.getting_start, "getting-started/");
        mPagePath.put(R.id.css, "css/");
        mPagePath.put(R.id.components, "components/");
        mPagePath.put(R.id.javascript, "javascript/");
    }

    private DrawerLayout mDrawerLayout = null;

    private WebView mWebView = null;

    private AlertDialog mProgressDialog = null;

    private NavigationView mNavigationView = null;

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
    }

    private void loadPage()
    {
        String url = mWebsites.get(mCurLangItem.getItemId())+mPagePath.get(mCurPageItem.getItemId());
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
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Log.e(TAG, "URL: " + url);
                if (url.equals("http://getbootstrap.com/getting-started#download")) {
                    return false;
                }
//                view.loadUrl(url);
                return true; //super.shouldOverrideUrlLoading(view, url);
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


    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
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
                loadPage();
                break;

            default:
                if (mCurLangItem == menuItem) {
                    break;
                }
                mCurLangItem.setChecked(false);
                mCurLangItem = menuItem;
                loadPage();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
