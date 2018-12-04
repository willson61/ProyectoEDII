package com.example.sthephan.proyectoedii;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterMensajes extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Mensaje> items;
    protected UsuarioItem usu;

    public AdapterMensajes(Activity activity, ArrayList<Mensaje> items, UsuarioItem data) {
        this.activity = activity;
        this.items = items;
        this.usu = data;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Mensaje> mensajes) {
        for (int i = 0; i < mensajes.size(); i++) {
            items.add(mensajes.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            try {
                holder.m = items.get(position);
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if(holder.m.getRemitente().equals(usu.getNombreUsuarioEmisor())){
                    v = inf.inflate(R.layout.my_message, parent, false);
                    holder.message = (TextView) v.findViewById(R.id.message_bodyS);
                }
                else{
                    v = inf.inflate(R.layout.their_message, parent, false);
                    holder.message = (TextView) v.findViewById(R.id.message_bodyR);
                    holder.name = (TextView) v.findViewById(R.id.name);
                }
                v.setTag(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder = (ViewHolder) v.getTag();
        }
        if(items.get(position).getRemitente().equals(usu.getNombreUsuarioEmisor())){
            holder.message.setText(holder.m.getMensaje());
        }
        else{
            holder.message.setText(holder.m.getMensaje());
            holder.name.setText(holder.m.getRemitente());
        }
        return v;
    }

    public class ViewHolder{
        public TextView name, message;
        public Mensaje m;
    }
}
