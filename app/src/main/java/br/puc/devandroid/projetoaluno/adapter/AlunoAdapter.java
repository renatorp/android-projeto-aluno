package br.puc.devandroid.projetoaluno.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.puc.devandroid.projetoaluno.R;
import br.puc.devandroid.projetoaluno.entity.Aluno;
import br.puc.devandroid.projetoaluno.service.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by renatorp on 09/09/16.
 */
public class AlunoAdapter extends BaseAdapter{

    private static final String TAG = "HTTP";
    private final Context context;
    private final List<Aluno> alunos;
    private ProgressDialog pDialog;
    private APIService apiService;

    public AlunoAdapter(Context context, APIService apiService, List<Aluno> alunos) {
        this.context = context;
        this.alunos = alunos;
        this.apiService = apiService;
    }

    @Override
    public int getCount() {
        return alunos != null ? alunos.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return alunos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_aluno, parent, false);

        TextView txtNome = (TextView) view.findViewById(R.id.txt_nome_aluno);
        TextView txtEndereco = (TextView) view.findViewById(R.id.txt_endereco_aluno);
        TextView txtIdade = (TextView) view.findViewById(R.id.txt_idade_aluno);
        ImageButton btnChamarAluno = (ImageButton) view.findViewById(R.id.btn_chamar_aluno);
        ImageButton btnExcluirAluno = (ImageButton) view.findViewById(R.id.btn_excluir_aluno);
        final ImageView imgImagemAluno = (ImageView) view.findViewById(R.id.img_aluno);

        final Aluno aluno = alunos.get(position);

        if (aluno.getNome() != null) {
            txtNome.setText(aluno.getNome());
        }

        if (aluno.getEndereco() != null) {
            txtEndereco.setText(aluno.getEndereco());
        }

        if (aluno.getIdade() != null) {
            txtIdade.setText(aluno.getIdade().toString());
        }

        btnChamarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+ aluno.getTelefone()));
                context.startActivity(callIntent);
            }
        });

        btnExcluirAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertaExcluir(aluno);
            }
        });

        Picasso.with(context)
                .load(aluno.getFotoUrl())
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.sem_imagem)
                .into(imgImagemAluno);


        LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.layoutDadosAluno) ;
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGoogleMaps(aluno);
            }
        });

        return view;
    }

    private void showAlertaExcluir(final Aluno aluno) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_menu_info_details);
        builder.setMessage(context.getResources().getString(R.string.message_excluir_aluno, aluno.getNome()));
        builder.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                excluirAluno(aluno);
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void excluirAluno(final Aluno aluno) {
        startDialog();
        Call<Void> call = apiService.deleteAluno(aluno.getObjectId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    alunos.remove(aluno);
                    Toast.makeText(context, context.getResources().getString(R.string.message_aluno_ecluido), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                } else {
                    Log.e(TAG, response.message());
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                pDialog.dismiss();
            }
        });
    }

    private void startDialog() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getResources().getString(R.string.dialog_caregando));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void showGoogleMaps(final Aluno aluno) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(aluno.getEndereco()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.msg_gmaps_indisponivel), Toast.LENGTH_SHORT).show();
        }
    }
}
