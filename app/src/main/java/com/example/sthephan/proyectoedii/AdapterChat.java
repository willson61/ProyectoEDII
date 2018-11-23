package com.example.sthephan.proyectoedii;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterChat extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<UsuarioItem> items;

    public AdapterChat(Activity activity, ArrayList<UsuarioItem> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<UsuarioItem> compresiones) {
        for (int i = 0; i < compresiones.size(); i++) {
            items.add(compresiones.get(i));
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
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.itemchatlist, parent, false);
                holder.c = items.get(position);
                holder.labelUsuario = (TextView) v.findViewById(R.id.labelUsuario);
                holder.labelMensaje = (TextView) v.findViewById(R.id.labelUltimoMensaje);
                v.setTag(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.labelUsuario.setText(holder.c.getNombreUsuario());
        holder.labelMensaje.setText("  " + holder.c.getUltimoMensaje());
        return v;
    }

    private class ViewHolder {
        private TextView labelUsuario;
        private TextView labelMensaje;
        private UsuarioItem c;
    }
}
