package com.voc4u.ws;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.voc4u.controller.DictionaryOpenHelper.RemovedWord;
import com.voc4u.controller.Word;
import com.voc4u.controller.WordController;
import com.voc4u.setting.CommonSetting;
import com.wildfuse.wilda.controller.AsyncManager;
import com.wildfuse.wilda.controller.IAsyncController;
import com.wildfuse.wilda.network.NetworkManager;
import com.wildfuse.wilda.network.Request;
import com.wildfuse.wilda.network.RequestException;
import com.wildfuse.wilda.network.Response;

public class DeleteWord {

	
	final WordController mWCtrl;
	public DeleteWord(Word w, WordController wc) 
	{
		mWCtrl = wc;
		new AsyncManager<Word, Boolean>(new Controller()).execute(w);
	}

	
	public DeleteWord(WordController wc)
	{
		mWCtrl = wc;
		
		
		new AsyncManager<Void, Boolean>(new ControllerMulti()).execute();
		
	}
	 
	
	protected boolean delete(Word w) throws RequestException
	{
		return delete(w.getLern().replace(",", "|"), w.getNative().replace(",", "|"), "");
	}
	
	protected boolean delete(RemovedWord rw) throws RequestException
	{
		boolean result = delete(rw.learn.replace(",", "|"), rw.nativ.replace(",", "|"), rw.ws_id);
		
		if(result)
		{
			mWCtrl.removeFromDeleteList(rw.id);
		}
		
		return result;
	}
	
	protected boolean delete(String learn, String nativ, String ws_id) throws RequestException
	{
		Request req = new Request4("del");
		req.addUrlParam("l", learn);
		req.addUrlParam("n", nativ);
		req.addUrlParam("lc", CommonSetting.lernCode.code);
		req.addUrlParam("nc", CommonSetting.nativeCode.code);
		Response resp = NetworkManager.execute(req);
		String resps = resp.getBody();
		try {
			JSONObject js = new JSONObject(resps);
			int error = js.getInt("error");
			if(error == 0)
			{
				//String ids = js.getString("word");
				//mWCtrl.updateWordWS(w.getId(), ids);
				return true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public class Controller implements IAsyncController<Word, Boolean> 
	{

		@Override
		public void onPreExecute() throws RequestException {
			// TODO Auto-generated method stub

		}

		@Override
		public Boolean doInBackground(Word... args) throws RequestException {
			
			return new Boolean(delete(args[0]));
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
	
	public class ControllerMulti implements IAsyncController<Void, Boolean> 
	{

		@Override
		public void onPreExecute() throws RequestException {
			// TODO Auto-generated method stub

		}

		@Override
		public Boolean doInBackground(Void... args) throws RequestException {
			// TODO: add mutext
			// TODO: test the conection
			ArrayList<RemovedWord> words = mWCtrl.getRemovedWords();
			if(words == null)
			{
				return new Boolean(false);
			}
			
			for(RemovedWord w : words)
			{
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				delete(w);
			}
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
