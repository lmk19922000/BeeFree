package com.blogspot.the3cube.beefree.logic.google;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gdata.client.DocumentQuery;
import com.google.gdata.client.GoogleAuthTokenFactory.UserToken;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.docs.PresentationEntry;
import com.google.gdata.data.docs.SpreadsheetEntry;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * This class handle the communication aspect with Google Docs.<br>
 * 
 * @author 		The3Cube
 * @since 		v0.2
 * @version 	v0.2
 * 
 */
public class GoogleDocs {
	private static enum DocumentType {
		DOCUMENT, PRESENTATION, SPREADSHEET
	};

	private final static String URL = "https://docs.google.com/feeds/default/private/full/";
	private final static String SPREADSHEET_DOWNLOAD_URL = "https://spreadsheets.google.com/feeds/download/spreadsheets";

	private String appName_ = "";
	private String username_, password_;

	private DocsService client_;

	private UserToken docsToken_;
	private UserToken spreadsheetsToken_;

	/**
	 * the constructor.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				11 October 2011
	 * @finish 				11 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param appName 		the name of the application accessing Google Docs.
	 */
	public GoogleDocs(String appName) {
		this.appName_ = appName;
	}

	/**
	 * this method set the authentication id for use later.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				11 October 2011
	 * @finish 				11 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param username 		the username of the account which you accessing.
	 * @param password 		the password matching the username
	 * @throws 				AuthenticationException
	 */
	public void authenticate(String username, String password)
			throws AuthenticationException {
		this.username_ = username;
		this.password_ = password;

		client_ = new DocsService(appName_);
		client_.setUserCredentials(username_, password_);
	}

	/**
	 * this method will retrieve the full list of documents.<br>
	 * by default, the API will return the first 100 documents in the user's
	 * documents list.<br>
	 * this is to save bandwidth and improve performance.<br>
	 * thus to retrieve the rest of the user's doc, the method need to page
	 * through the result.<br>
	 * this is done by following the <code>feed.getNextLink().getHref()</code>.
	 * this is the reason why this method is slightly different with the method
	 * with query input.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		11 October 2011
	 * @finish 		11 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @return 		the full list of documents.
	 * @throws 		IOException
	 * @throws 		ServiceException
	 */
	public DocumentListFeed getListOfDocument() throws IOException,
			ServiceException {
		DocumentQuery query = new DocumentQuery(new URL(URL));

		DocumentListFeed allEntries = new DocumentListFeed();
		DocumentListFeed tempFeed = client_.getFeed(query,
				DocumentListFeed.class);
		do {
			allEntries.getEntries().addAll(tempFeed.getEntries());
			Link nextLink = tempFeed.getNextLink();
			if ((nextLink == null) || (tempFeed.getEntries().size() == 0)) {
				break;
			}
			tempFeed = client_.getFeed(new URL(nextLink.getHref()),
					DocumentListFeed.class);
		} while (true);

		int totalEntries = allEntries.getEntries().size();

		return allEntries;
	}

	/**
	 * this method will the list of documents matching the query string.<br>
	 * this method does not perform the exact title search.<br>
	 * it uses the query string to search the feed.<br>
	 * the search is not case-sensitive and multiple documents may have the same
	 * name.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				11 October 2011
	 * @finish 				11 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param queryString 	the string which you want to search for.
	 * @param maxResults 	the maximum number of result that it will return.
	 * @return 				the feed is there is any.
	 * @throws 				IOException
	 * @throws 				ServiceException
	 */
	public DocumentListFeed getListOfDocument(String queryString, int maxResults)
			throws IOException, ServiceException {
		URL feedUri = new URL(URL);

		DocumentQuery query = new DocumentQuery(feedUri);
		query.setTitleQuery(queryString);
		query.setTitleExact(false);
		query.setMaxResults(10);
		DocumentListFeed listOfDocuments = client_.getFeed(query,
				DocumentListFeed.class);

		return listOfDocuments;
	}

	/**
	 * this method perform a search manually using string matching.<br>
	 * you would need to supply it a feed retrieve from either method.<br>
	 * it would search and return true is a string is found.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			11 October 2011
	 * @finish 			11 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @param feed 		the feed to search in.
	 * @param regex 	the string to match.
	 * @param exact 	if you want to match the exact title.
	 * @return 			if a document in the feed is found.
	 */
	public boolean searchFeed(DocumentListFeed feed, String regex, boolean exact) {

		boolean found = false;
		for (DocumentListEntry entry : feed.getEntries()) {
			String title = entry.getTitle().getPlainText();
			if (exact) {
				if (title.equals(regex)) {
					found = true;
					break;
				}
			} else {
				if (title.indexOf(regex) > -1) {
					found = true;
					break;
				}
			}

		}

		return found;
	}

	/**
	 * this method will print out the feed.<br>
	 * 
	 * @programmer 	Chua Jie Sheng
	 * @start 		11 October 2011
	 * @finish 		11 October 2011
	 * 
	 * @reviewer 	The3Cube
	 * @reviewdate 	25 October 2011
	 * 
	 * @since 		v0.2
	 * 
	 * @param feed 	the feed you want the method to print.
	 * @throws 		IOException
	 * @throws 		ServiceException
	 */
	public void printDocuments(DocumentListFeed feed) throws IOException,
			ServiceException {
		for (DocumentListEntry entry : feed.getEntries()) {
			String resourceId = entry.getResourceId();
			String resourceTitle = entry.getTitle().getPlainText();
		}
	}

	/**
	 * this method create a new document with the type you supplied.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			11 October 2011
	 * @finish 			11 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @param type 		the type of document to create.
	 * @param title 	the title of the document.
	 * @return 			the entry matching the type.
	 * @throws 			IOException
	 * @throws 			ServiceException
	 */
	public DocumentListEntry createNewDocument(DocumentType type, String title)
			throws IOException, ServiceException {
		DocumentListEntry newEntry = null;
		if (type == DocumentType.DOCUMENT) {
			newEntry = new DocumentEntry();
		} else if (type == DocumentType.PRESENTATION) {
			newEntry = new PresentationEntry();
		} else if (type == DocumentType.SPREADSHEET) {
			newEntry = new SpreadsheetEntry();
		}
		newEntry.setTitle(new PlainTextConstruct(title));

		return client_.insert(new URL(URL), newEntry);
	}

	/**
	 * this method will upload the file specified by the file path.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			11 October 2011
	 * @finish 			11 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @param filepath 	the location of the file to be uploaded.
	 * @param title 	the name of the document in Google Docs.
	 * @return 			the updated entry return by the service.
	 * @throws 			IOException
	 * @throws 			ServiceException
	 */
	public DocumentListEntry uploadFile(String filepath, String title)
			throws IOException, ServiceException {
		File file = new File(filepath);
		String mimeType = DocumentListEntry.MediaType.fromFileName(
				file.getName()).getMimeType();

		DocumentListEntry newDocument = new DocumentListEntry();
		newDocument.setFile(file, mimeType);
		newDocument.setTitle(new PlainTextConstruct(title));

		return client_.insert(new URL(URL), newDocument);
	}

	/**
	 * this method will first authenticate for the spreadsheet token then
	 * download the spreadsheet.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				11 October 2011
	 * @finish 				11 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param resourceId 	the resource id of the spreadsheet to download.
	 * @param filepath 		the path which the spreadsheet will be saved.
	 * @throws 				MalformedURLException
	 * @throws 				IOException
	 * @throws 				ServiceException
	 */
	public void authAndDownloadSpreadsheet(String resourceId, String filepath)
			throws MalformedURLException, IOException, ServiceException {
		spreadSheetAuthentication();
		downloadSpreadsheet(resourceId, filepath);
		restoreDocsToken();
	}

	/**
	 * this method will first authenticate for the spreadsheet token then
	 * download the spreadsheet.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			11 October 2011
	 * @finish 			11 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @param entry 	the entry which contain the spreadsheet which you want to
	 *            		download.
	 * @param filepath 	the path which you want to save the file.
	 * @throws 			ServiceException
	 * @throws 			IOException
	 * @throws 			MalformedURLException
	 */
	public void authAndDownloadSpreadsheet(DocumentListEntry entry,
			String filepath) throws MalformedURLException, IOException,
			ServiceException {
		spreadSheetAuthentication();
		downloadSpreadsheet(entry, filepath);
		restoreDocsToken();
	}

	/**
	 * this method substitude the spreadsheet token for the docs token.<br>
	 * the spreadsheet token is needed to download the spreadsheet.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			11 October 2011
	 * @finish 			11 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @throws 			AuthenticationException
	 */
	private void spreadSheetAuthentication() throws AuthenticationException {
		SpreadsheetService spreadClient = new SpreadsheetService(appName_);
		spreadClient.setUserCredentials(username_, password_);

		// Substitute the spreadsheets token for the docs token
		docsToken_ = (UserToken) client_.getAuthTokenFactory().getAuthToken();
		spreadsheetsToken_ = (UserToken) spreadClient.getAuthTokenFactory()
				.getAuthToken();
		client_.setUserToken(spreadsheetsToken_.getValue());
	}

	/**
	 * this method restore the docs token for DocList client.<br>
	 * 
	 * @programmer 		Chua Jie Sheng
	 * @start 			11 October 2011
	 * @finish 			11 October 2011
	 * 
	 * @reviewer 		The3Cube
	 * @reviewdate 		25 October 2011
	 * 
	 * @since 			v0.2
	 * 
	 * @since 			v0.2
	 */
	private void restoreDocsToken() {
		client_.setUserToken(docsToken_.getValue());
	}

	/**
	 * this method will download the spreadsheet given it's resource id and the
	 * filepath to be saved.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				11 October 2011
	 * @finish 				11 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param resourceId 	the resource id of the spreadsheet to download.
	 * @param filepath 		the path which the spreadsheet will be saved.
	 * @throws 				IOException
	 * @throws 				MalformedURLException
	 * @throws 				ServiceException
	 */
	private void downloadSpreadsheet(String resourceId, String filepath)
			throws IOException, MalformedURLException, ServiceException {

		String docId = resourceId.substring(resourceId.lastIndexOf(":") + 1);
		String fileExtension = filepath
				.substring(filepath.lastIndexOf(".") + 1);
		String exportUrl = SPREADSHEET_DOWNLOAD_URL + "/Export?key=" + docId
				+ "&exportFormat=" + fileExtension;

		// If exporting to .csv or .tsv, add the gid parameter to specify which
		// sheet to export
		if (fileExtension.equals("csv") || fileExtension.equals("tsv")) {
			exportUrl += "&gid=0"; // gid=0 will download only the first sheet
		}

		downloadFile(exportUrl, filepath);
	}

	/**
	 * this method will download the spreadsheet using the entry instead of the
	 * resource id.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				11 October 2011
	 * @finish 				11 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param entry 		the entry which contain the spreadsheet which you want to
	 *            			download.
	 * @param filepath 		the path which you want to save the file.
	 * @throws 				IOException
	 * @throws 				MalformedURLException
	 * @throws 				ServiceException
	 */
	private void downloadSpreadsheet(DocumentListEntry entry, String filepath)
			throws IOException, MalformedURLException, ServiceException {
		String fileExtension = filepath
				.substring(filepath.lastIndexOf(".") + 1);
		String exportUrl = ((MediaContent) entry.getContent()).getUri()
				+ "&exportFormat=" + fileExtension;

		// If exporting to .csv or .tsv, add the gid parameter to specify which
		// sheet to export
		if (fileExtension.equals("csv") || fileExtension.equals("tsv")) {
			exportUrl += "&gid=0"; // gid=0 will download only the first sheet
		}

		downloadFile(exportUrl, filepath);
	}

	/**
	 * generalise method to download a file from Google Docs and saving it to
	 * the path.<br>
	 * 
	 * @programmer 			Chua Jie Sheng
	 * @start 				11 October 2011
	 * @finish 				11 October 2011
	 * 
	 * @reviewer 			The3Cube
	 * @reviewdate 			25 October 2011
	 * 
	 * @since 				v0.2
	 * 
	 * @param exportUrl 	the url to download from.
	 * @param filepath 		the file path to be saved.
	 * @throws 				IOException
	 * @throws 				MalformedURLException
	 * @throws 				ServiceException
	 */
	public void downloadFile(String exportUrl, String filepath)
			throws IOException, MalformedURLException, ServiceException {
		MediaContent mc = new MediaContent();
		mc.setUri(exportUrl);
		MediaSource ms = client_.getMedia(mc);

		InputStream inStream = null;
		FileOutputStream outStream = null;

		try {
			inStream = ms.getInputStream();
			outStream = new FileOutputStream(filepath);

			int c;
			while ((c = inStream.read()) != -1) {
				outStream.write(c);
			}
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (outStream != null) {
				outStream.flush();
				outStream.close();
			}
		}
	}

	/**
	 * @return the appName_.
	 */
	public String getAppName_() {
		return appName_;
	}

	/**
	 * @param appName_
	 *            the appName_ to set.
	 */
	public void setAppName_(String appName_) {
		this.appName_ = appName_;
	}
}
