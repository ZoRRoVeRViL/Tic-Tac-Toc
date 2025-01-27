package com.example.tictactoe;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    private int turn = 0;
    public static String firstMark = "X";
    public static String secondMark = "O";
    public final int SIZE = 3;
    private String[][] matrix = new String[SIZE][SIZE];
    private TextView resultTextView;
    private final List<Button> buttons = new ArrayList<>();
    private SwitchMaterial switchOpponent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        resultTextView = findViewById(R.id.resultTextView);
        LinearLayout layoutForButtons = findViewById(R.id.LayoutForButtons);

        for (int i = 0; i < layoutForButtons.getChildCount(); i++)
        {
            LinearLayout layout = findViewById(layoutForButtons.getChildAt(i).getId());

            for (int j = 0; j < layout.getChildCount(); j++)
            {
                Button button = findViewById(layout.getChildAt(j).getId());
                buttons.add(button);
            }
        }

        switchOpponent = findViewById(R.id.switchOpponent);

        Button restart = findViewById(R.id.restart);
        restart.setOnClickListener(this::clickOnRestart);
    }

    public boolean isFilled()
    {
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                if (!Objects.equals(matrix[i][j], firstMark) && !Objects.equals(matrix[i][j], secondMark))
                    return false;
            }
        }

        return true;
    }

    public boolean checkWin(String XorO)
    {
        StringBuilder expect = new StringBuilder();

        for (int index = 0; index < SIZE; index++)
        {
            expect.append(XorO);
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                if (matrix[i][j] != null)
                {
                    result.append(matrix[i][j]);
                }
            }

            if (expect.toString().equals(result.toString()))
            {
                return true;
            }
            else
                result = new StringBuilder();
        }

        for (int j = 0; j < SIZE; j++)
        {
            for (int i = 0; i < SIZE; i++)
            {
                if (matrix[i][j] != null)
                {
                    result.append(matrix[i][j]);
                }
            }

            if (expect.toString().equals(result.toString()))
            {
                return true;
            }
            else
                result = new StringBuilder();
        }

        for (int j = 0, i = 0; j < SIZE; j++, i++)
        {
            if (matrix[i][j] != null)
            {
                result.append(matrix[i][j]);
            }
        }

        if (expect.toString().equals(result.toString()))
        {
            return true;
        }
        else
            result = new StringBuilder();

        for (int i = SIZE - 1, j = 0; j < SIZE; j++, i--)
        {
            if (matrix[i][j] != null)
            {
                result.append(matrix[i][j]);
            }
        }

        return expect.toString().equals(result.toString());
    }

    public void clickOnButton(String buttonTag)
    {
        if (matrix[Character.digit(buttonTag.charAt(0), 10)][Character.digit(buttonTag.charAt(1), 10)] == null
                && !checkWin(firstMark) && !checkWin(secondMark))
        {
            matrix[Character.digit(buttonTag.charAt(0), 10)][Character.digit(buttonTag.charAt(1), 10)] = getValue();
            setTurn(getTurn() + 1);
        }
    }

    public int clickOnButtonWithAI()
    {
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                if (!(Objects.equals(matrix[i][j], firstMark)) && !(Objects.equals(matrix[i][j], secondMark)))
                {
                    matrix[i][j] = secondMark;

                    if (checkWin(secondMark))
                        return i * SIZE + j;

                    matrix[i][j] = null;
                    temp.add(i * SIZE + j);
                }
            }
        }

        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                if (!(Objects.equals(matrix[i][j], firstMark)) && !(Objects.equals(matrix[i][j], secondMark)))
                {
                    matrix[i][j] = firstMark;

                    if (checkWin(firstMark))
                    {
                        matrix[i][j] = secondMark;
                        return i * SIZE + j;
                    }

                    matrix[i][j] = null;
                }
            }
        }

        int random = temp.get(new Random().nextInt(temp.size()));
        matrix[random / 3][random % 3] = secondMark;

        return random;
    }

    public String[][] getMatrix()
    {
        return matrix;
    }

    public void clearMatrix()
    {
        matrix = new String[SIZE][SIZE];
        setTurn(0);
    }

    public String getValue()
    {
        return turn % 2 == 0 ? firstMark : secondMark;
    }

    public int getTurn()
    {
        return this.turn;
    }

    public void setTurn(int turn)
    {
        this.turn = turn;
    }

    public boolean isWin()
    {
        String resultMessage = "";

        if (this.checkWin("X"))
        {
            resultMessage = "Х- Победили Крестики! -Х";
        }
        else if (this.checkWin("O"))
        {
            resultMessage = "О- Победили Нолики! -О";
        }
        else if (this.isFilled())
        {
            resultMessage = "Ничья!";
        }

        if (!resultMessage.isEmpty())
        {
            resultTextView.setText(resultMessage);

            return true;
        }

        return false;
    }

    public void clickOnButton(View view)
    {
        Button clicked = findViewById(view.getId());
        if (clicked.getText() != firstMark && clicked.getText() != secondMark && !isWin())
        {
            clicked.setText(getValue());
            clickOnButton((String) clicked.getTag());

            if (!switchOpponent.isChecked() && !isFilled() && !checkWin(firstMark) && getTurn() % 2 == 1)
            {
                Button button = buttons.get(clickOnButtonWithAI());
                button.setText(getValue());
                setTurn(getTurn() + 1);
            }

            isWin();
        }
    }

    public void clickOnRestart(View view)
    {
        for (Button v : buttons)
        {
            v.setText("");
        }

        clearMatrix();
        resultTextView.setText("");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        for (Button v : buttons)
        {
            outState.putString("buttons" + v.getId(), (String) v.getText());
        }

        for (int i = 0; i < getMatrix().length; i++)
        {
            for (int j = 0; j < getMatrix().length; j++)
            {
                outState.putString("matrix" + ((i * SIZE) + j), getMatrix()[i][j]);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        for (Button v : buttons)
        {
            v.setText(savedInstanceState.getString("buttons" + v.getId()));
        }

        for (int i = 0; i < getMatrix().length; i++)
        {
            for (int j = 0; j < getMatrix().length; j++)
            {
                getMatrix()[i][j] = savedInstanceState.getString("matrix" + ((i * SIZE) + j));
            }
        }
    }
}