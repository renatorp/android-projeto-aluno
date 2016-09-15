package br.puc.devandroid.projetoaluno;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import br.puc.devandroid.projetoaluno.adapter.AlunoAdapter;
import br.puc.devandroid.projetoaluno.entity.Aluno;
import br.puc.devandroid.projetoaluno.entity.AlunosResult;
import br.puc.devandroid.projetoaluno.service.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "HTTP";
    private ProgressDialog pDialog;
    private List<Aluno> alunos;
    private APIService apiService;
    private ListView listViewAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogNovoAluno();
            }
        });
        initializeAPIService();
        initilizeListAlunos();
    }

    private void showDialogNovoAluno() {
        LayoutInflater inflater=getLayoutInflater();
        final View view =inflater.inflate(R.layout.layout_novo_aluno, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_edit);
        builder.setTitle(getResources().getString(R.string.title_novo_aluno));
        builder.setView(view);

        builder.setPositiveButton(R.string.btn_salvar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Aluno aluno = extractAlunoFromView(view);
                salvarAluno(aluno);
            }
        });
        builder.setNegativeButton(R.string.btn_cancelar, null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void salvarAluno(final Aluno aluno) {
        Call<Aluno> call = apiService.createAluno(aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    aluno.setObjectId(response.body().getObjectId());
                    alunos.add(aluno);
                    showAlunos(alunos);

                    Toast.makeText(getBaseContext(), getResources().getString(R.string.msg_aluno_adicionado, aluno.getNome()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                Log.e(TAG, "Ocorreu um erro ao salvar o aluno");
            }
        });

    }

    private Aluno extractAlunoFromView(View view) {

        Aluno aluno = new Aluno();

        EditText edtNome = (EditText) view.findViewById(R.id.edt_nome_novo_aluno);
        aluno.setNome(edtNome.getText().toString());

        EditText edtIdade = (EditText) view.findViewById(R.id.edt_idade_novo_aluno);
        aluno.setIdade(Integer.valueOf(edtIdade.getText().toString()));

        EditText edtFoto = (EditText) view.findViewById(R.id.edt_foto_novo_aluno);
        aluno.setFotoUrl(edtFoto.getText().toString());

        EditText edtTel = (EditText) view.findViewById(R.id.edt_telefone_novo_aluno);
        aluno.setTelefone(edtTel.getText().toString());

        EditText edtEndereco = (EditText) view.findViewById(R.id.edt_endereco_novo_aluno);
        aluno.setEndereco(edtEndereco.getText().toString());

        return aluno;
    }

    private void initializeAPIService() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(APIService.class);
    }

    private void initilizeListAlunos() {
        listViewAlunos = (ListView) findViewById(R.id.listViewAlunos);

        startDialog();

        Call<AlunosResult> call = apiService.findAlunos();
        call.enqueue(new Callback<AlunosResult>() {
            @Override
            public void onResponse(Call<AlunosResult> call, Response<AlunosResult> response) {
                if (response.isSuccessful()) {
                    alunos = response.body().getResults();
                    showAlunos(alunos);
                } else {
                    Log.e(TAG, response.message());
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AlunosResult> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                pDialog.dismiss();
            }
        });

    }

    private void startDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.dialog_caregando));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void showAlunos(List<Aluno> alunos) {
        AlunoAdapter alunosAdapter = new AlunoAdapter(this, apiService, alunos);
        listViewAlunos.setAdapter(alunosAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
