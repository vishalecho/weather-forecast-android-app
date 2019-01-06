package io.github.vishalecho.android.forecast.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.github.vishalecho.android.forecast.R;
import io.github.vishalecho.android.forecast.models.DataObject;
import io.github.vishalecho.android.forecast.utilities.Constants;

/**
 * @author vishal kumar
 * @version 1.0
 * @since 06.01.2019
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<DataObject> dataSet;

    public CardAdapter(List<DataObject> data) {
        this.dataSet = data;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_weather, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, final int listPosition) {
        DataObject thisObject = dataSet.get(listPosition);
        holder.timeStamp.setText(getTime(thisObject.getDt() * 1000));
        holder.textViewDescription.setText(thisObject.getWeather().get(0).getDescription());
        holder.textViewWind.setText("Wind: " + thisObject.getWind().getSpeed() + " m/s");
        holder.textViewPressure.setText("Pressure: " + thisObject.getMain().getPressure() + " hPa");
        holder.textViewHumidity.setText("Humidity: " + thisObject.getMain().getHumidity() + " %");
        holder.textViewTemperature.setText(thisObject.getMain().getTemp() + Constants.TEMP_UNIT);
        switch (thisObject.getWeather().get(0).getIcon()) {
            case "01d":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_clear_sky);
                holder.cardView.setBackgroundResource(R.color.color_clear_and_sunny);
                break;
            case "01n":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_clear_sky);
                holder.cardView.setBackgroundResource(R.color.color_clear_and_sunny);
                break;
            case "02d":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_few_cloud);
                holder.cardView.setBackgroundResource(R.color.color_partly_cloudy);
                break;
            case "02n":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_few_cloud);
                holder.cardView.setBackgroundResource(R.color.color_partly_cloudy);
                break;
            case "03d":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_scattered_clouds);
                holder.cardView.setBackgroundResource(R.color.color_gusty_winds);
                break;
            case "03n":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_scattered_clouds);
                holder.cardView.setBackgroundResource(R.color.color_gusty_winds);
                break;
            case "04d":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_broken_clouds);
                holder.cardView.setBackgroundResource(R.color.color_cloudy_overnight);
                break;
            case "04n":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_broken_clouds);
                holder.cardView.setBackgroundResource(R.color.color_cloudy_overnight);
                break;
            case "09d":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_shower_rain);
                holder.cardView.setBackgroundResource(R.color.color_hail_stroms);
                break;
            case "09n":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_shower_rain);
                holder.cardView.setBackgroundResource(R.color.color_hail_stroms);
                break;
            case "10d":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_rain);
                holder.cardView.setBackgroundResource(R.color.color_heavy_rain);
                break;
            case "10n":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_rain);
                holder.cardView.setBackgroundResource(R.color.color_heavy_rain);
                break;
            case "11d":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_thunderstorm);
                holder.cardView.setBackgroundResource(R.color.color_thunderstroms);
                break;
            case "11n":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_thunderstorm);
                holder.cardView.setBackgroundResource(R.color.color_thunderstroms);
                break;
            case "13d":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_snow);
                holder.cardView.setBackgroundResource(R.color.color_snow);
                break;
            case "13n":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_snow);
                holder.cardView.setBackgroundResource(R.color.color_snow);
                break;
            case "15d":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_mist);
                holder.cardView.setBackgroundResource(R.color.color_mix_snow_and_rain);
                break;
            case "15n":
                holder.imageViewIcon.setImageResource(R.drawable.ic_weather_mist);
                holder.cardView.setBackgroundResource(R.color.color_mix_snow_and_rain);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private String getTime(Long milliTime) {
        Date currentDate = new Date(milliTime);
        SimpleDateFormat df = new SimpleDateFormat("E, dd.MM.yyyy - hh:mm a");
        String date = df.format(currentDate);
        return date;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView timeStamp;
        TextView textViewDescription;
        TextView textViewWind;
        TextView textViewPressure;
        TextView textViewHumidity;
        ImageView imageViewIcon;
        TextView textViewTemperature;


        public CardViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.timeStamp = (TextView) itemView.findViewById(R.id.timeStamp);
            this.textViewDescription = (TextView) itemView.findViewById(R.id.textDescription);
            this.textViewWind = (TextView) itemView.findViewById(R.id.wind);
            this.textViewPressure = (TextView) itemView.findViewById(R.id.pressure);
            this.textViewHumidity = (TextView) itemView.findViewById(R.id.humidity);
            this.textViewTemperature = (TextView) itemView.findViewById(R.id.temp);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
