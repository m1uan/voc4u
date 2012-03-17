package com.voc4u.ws;

import org.json.JSONException;
import org.json.JSONObject;

import com.voc4u.controller.Word;
import com.voc4u.controller.WordController;
import com.voc4u.setting.CommonSetting;
import com.wildfuse.wilda.controller.AsyncManager;
import com.wildfuse.wilda.controller.IAsyncController;
import com.wildfuse.wilda.network.NetworkManager;
import com.wildfuse.wilda.network.Request;
import com.wildfuse.wilda.network.RequestException;
import com.wildfuse.wilda.network.Response;

public class AddWord {

	final long mId;
	final WordController mWCtrl;
	public AddWord(Word w, long id, WordController wc) 
	{
		mId = id;
		mWCtrl = wc;
		new AsyncManager<Word, Boolean>(new Controller()).execute(w);
	}

	 
	
	protected void add(Word w) throws RequestException
	{
		Request req = new Request4("add");
		req.addUrlParam("l", w.getLern().replace(",", "|"));
		req.addUrlParam("n", w.getNative().replace(",", "|"));
		req.addUrlParam("lc", CommonSetting.lernCode.code);
		req.addUrlParam("nc", CommonSetting.nativeCode.code);
		Response resp = NetworkManager.execute(req);
		String resps = resp.getBody();
		w.getId();
		try {
			JSONObject js = new JSONObject(resps);
			int error = js.getInt("error");
			if(error == 0)
			{
				String ids = js.getString("word");
				mWCtrl.updateWordWS(mId, ids);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class Controller implements IAsyncController<Word, Boolean> 
	{

		@Override
		public void onPreExecute() throws RequestException {
			// TODO Auto-generated method stub

		}

		@Override
		public Boolean doInBackground(Word... args) throws RequestException {
			add(args[0]);
			return new Boolean(true);
		}

		@Override
		public void onBackgroundJobComplete(Boolean result) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFailure(RequestException e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPostExecute() {
			// TODO Auto-generated method stub

		}

	}
}
