package com.example.eventec;

import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;
        import java.util.List;

public class QuestionAdapter extends ArrayAdapter<Question> {

    private Context context;
    private List<Question> questionList;

    public QuestionAdapter(Context context, List<Question> questionList) {
        super(context, R.layout.list_item_question, questionList);
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // Inflar el diseño de elemento de lista si es necesario
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_question, parent, false);

            // Inicializar el ViewHolder para mantener referencias a los elementos de la vista
            viewHolder = new ViewHolder();
            viewHolder.questionTextTextView = convertView.findViewById(R.id.questionTextTextView);
            viewHolder.userNameTextView = convertView.findViewById(R.id.userNameTextView);

            convertView.setTag(viewHolder);
        } else {
            // Reutilizar el ViewHolder si ya existe
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Obtener la pregunta actual
        Question question = questionList.get(position);

        // Establecer los datos en los elementos de la vista
        viewHolder.questionTextTextView.setText(question.getText());
        viewHolder.userNameTextView.setText("Usuario: " + question.getUserName());

        return convertView;
    }

    // Clase ViewHolder para mantener referencias a elementos de vista
    private static class ViewHolder {
        TextView questionTextTextView;
        TextView userNameTextView;
    }
}
