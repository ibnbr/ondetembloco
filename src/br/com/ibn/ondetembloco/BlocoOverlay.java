package br.com.ibn.ondetembloco;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class BlocoOverlay extends BalloonItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> itens = new ArrayList<OverlayItem>();
	private Context ctx;

	public BlocoOverlay(Drawable defaultMarker, Context ctx, MapView mv) {
		super(boundCenterBottom(defaultMarker), mv);
		setBalloonBottomOffset(ctx.getResources().getDrawable(R.drawable.map_pin2).getIntrinsicHeight()-2);
		this.ctx = ctx;
	}

	public void addOverlay(OverlayItem item) {
		itens.add(item);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return itens.get(i);
	}

	@Override
	public int size() {
		return itens.size();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {

		Intent i = new Intent(Intent.ACTION_VIEW);
		String latlon = item.getPoint().getLatitudeE6() / 1e6 + ","
				+ item.getPoint().getLongitudeE6() / 1e6;
		i.setData(Uri.parse("geo:" + latlon + "?z=18&q=" + latlon + "("
				+ item.getTitle() + ")"));

		ctx.startActivity(i);

		return true;
	}
}
