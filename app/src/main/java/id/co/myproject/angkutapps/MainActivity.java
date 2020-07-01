package id.co.myproject.angkutapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.angkutapps.view.HomeFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    public static BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.frame_home);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_nav){
//                    setFragment(new NearbyMapsFragment());
                }else if (item.getItemId() == R.id.history_nav){
                    setFragment(new HistoryFragment());
                }else if (item.getItemId() == R.id.payment_nav){
                    setFragment(new PaymentFragment());
                }else if (item.getItemId() == R.id.akun_nav){
                    setFragment(new ProfilFragment());
                }else if (item.getItemId() == R.id.barcode_nav){
                    setFragment(new BarcodeFragment());
                }

                return true;
            }
        });

        setFragment(new HomeFragment());

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), fragment);
        transaction.commit();
    }
}
