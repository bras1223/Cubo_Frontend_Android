package nl.cubo.cubo;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import nl.cubo.cubo.challenge.DragDropFragment;

public class HomeActivity extends AppCompatActivity implements DragDropFragment.OnDropListener {

    private DragDropFragment f1, f2, f3, f4, f5;
    private ArrayList<DragDropFragment> fragments = new ArrayList<>();
    private TextView timer;
    private int currentNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this

        setContentView(R.layout.activity_home);
        timer = findViewById(R.id.textTimer);
        f1 = new DragDropFragment();
        f2 = new DragDropFragment();
        f3 = new DragDropFragment();
        f4 = new DragDropFragment();
        f5 = new DragDropFragment();

        f1.setNumber(1);
        f1.setActive(true);
        f2.setNumber(2);
        f3.setNumber(3);
        f4.setNumber(4);
        f5.setNumber(5);

        f1.setOnDropListener(this);
        f2.setOnDropListener(this);
        f3.setOnDropListener(this);
        f4.setOnDropListener(this);
        f5.setOnDropListener(this);

        fragments.add(f1);
        fragments.add(f2);
        fragments.add(f3);
        fragments.add(f4);
        fragments.add(f5);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.drag1, f1)
                .replace(R.id.drag2, f2)
                .replace(R.id.drag3, f3)
                .replace(R.id.drag4, f4)
                .replace(R.id.drag5, f5)
                .commit();

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                for(DragDropFragment f : fragments) {
                    f.setActive(false);
                    f.stopDrag();
                }
            }
        }.start();

    }

    @Override
    public void onDragFinished() {
        currentNumber += 1;
        checkProgress();
    }

    private void checkProgress() {
        if(currentNumber < 6) {
            updateFragments();
        } else {
            int sequence[] = {1, 2, 3, 4, 5};
            shuffleArray(sequence);

            this.currentNumber = 1;
            for(DragDropFragment f :fragments) {
                f.resetState(sequence[f.getNumber() - 1 ]);
                if(f.getNumber() == 1) {
                    f.setActive(true);
                } else {
                    f.setActive(false);
                }
            }
        }
    }

    private void updateFragments() {
        for(DragDropFragment f :fragments) {
            if(f.getNumber() == currentNumber) {
                f.setActive(true);
            }
        }

    }

    static void shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
