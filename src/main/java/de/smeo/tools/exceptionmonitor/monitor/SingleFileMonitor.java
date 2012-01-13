package de.smeo.tools.exceptionmonitor.monitor;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.List;

import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionParser;

/**
 * Responsible for reading a single log file. It continues on the file position it stopped
 * last time checking the file. 
 * The file is read in chunks.
 * 
 * The monitor is preparing a all day report and a change report (getExceptionsSinceLastUpdateAndReset)
 * 
 * @author smeo
 *
 */
public class SingleFileMonitor {
	private long lastFileCheckTime = -1;
	private ExceptionParser exceptionParser = new ExceptionParser();
	private File monitoredFile;
	private FileMonitorState fileMonitorState;
	private FileChunkReader fileChunkReader;
	
	public SingleFileMonitor(File monitoredFile) {
		this.monitoredFile = monitoredFile;
		fileChunkReader = new FileChunkReader();
	}
	
	

	public FileMonitorState getFileMonitorState() {
		this.fileMonitorState.lastReadLineIndex = exceptionParser.getCurrLineIndex();
		return this.fileMonitorState;
	}

	public synchronized void setFileMonitorState(FileMonitorState fileMonitorState){	
		this.fileMonitorState = fileMonitorState;
		exceptionParser.setCurrLineIndex(fileMonitorState.lastReadLineIndex);
	}
	
	public synchronized List<ExceptionCausedByChain> parseNewFileEntriesAndReturnExceptions() 
	{
		lastFileCheckTime = getCurrTime();
		exceptionParser.clean();
		String nextLogFileChunk = null;
		while ( (nextLogFileChunk = getNextLogFileChunk()) != null){
			exceptionParser.parse(nextLogFileChunk);
		}
		return exceptionParser.getExceptionChains();
	}
	
	protected String getNextLogFileChunk() {
		return fileChunkReader.getNextChunk();
	}

	public long getLastFileCheckTime() {
		return this.lastFileCheckTime;
	}
	
	protected long getCurrTime() {
		return System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "SingleFileMonitor [monitoredFile=" + monitoredFile + "]";
	}

	
	public class FileChunkReader {
		private int MAX_CHUNK_SIZE = 1024 * 100;

		private String lastLineOfLastChunk;


		/**
		 * getun the next chunk of full filens from a file (eding with a \n)
		 * @return
		 */
		public String getNextChunk() {
			if (monitoredFile.exists()){
				try {
					RandomAccessFile randomAccessFile = new RandomAccessFile(monitoredFile, "r");
					resetFilePositionIfFileGotSmaller();
					
					byte[] destinationArray = new byte[MAX_CHUNK_SIZE];
					randomAccessFile.seek(fileMonitorState.lastReadFilePosition);
					int readBytes = randomAccessFile.read(destinationArray, 0, MAX_CHUNK_SIZE);
					randomAccessFile.close();

					StringBuffer newChunk = new StringBuffer();
					if (lastLineOfLastChunk != null){
						newChunk.append(lastLineOfLastChunk);
						lastLineOfLastChunk = null;
					}
					
					if (readBytes != -1){
						fileMonitorState.lastReadFilePosition = fileMonitorState.lastReadFilePosition+readBytes;
						
						String stringFromReadArray = createStringFromArray(destinationArray, readBytes);
						lastLineOfLastChunk = addStringWithoutLastLine(newChunk, stringFromReadArray);
						
					}

					if (newChunk.length() > 0){
						return newChunk.toString();
					}

				}  catch (Exception e) {
					e.printStackTrace();
					System.err.println("problem while reading file: " + monitoredFile.getAbsoluteFile() + " will stop to read");
				}
			}
			
			return null;
		}

		private String addStringWithoutLastLine(StringBuffer newChunk, String stringFromReadArray) {
			int positionOfLastCR = stringFromReadArray.lastIndexOf("\n");
			newChunk.append(stringFromReadArray.substring(0, positionOfLastCR+1));
			return stringFromReadArray.substring(positionOfLastCR+1);
		}

		private String createStringFromArray(byte[] destinationArray, int readBytes) {
			String stringFromByteArray; 
			if (readBytes == MAX_CHUNK_SIZE){
				stringFromByteArray = new String(destinationArray);
			} else {
				byte[] onlyReadBytes = new byte[readBytes];
				for (int i=0; i < readBytes; i++){
					onlyReadBytes[i] = destinationArray[i];
				}
				stringFromByteArray = new String(onlyReadBytes);
			}
			return stringFromByteArray;
		}

		private void resetFilePositionIfFileGotSmaller() {
			if (monitoredFile.length() < fileMonitorState.lastFileSize){
				System.out.println("Monitored log file got smaller, start from beginning '"+monitoredFile.getAbsolutePath()+"'");
				fileMonitorState.lastReadFilePosition = 0;
				fileMonitorState.lastReadLineIndex = 0;
			}
		}

	}
	
	public static class FileMonitorState implements Serializable {
		private static final long serialVersionUID = 825051722073708101L;
		public long lastReadFilePosition = 0;
		public long lastFileSize = 0;
		public long lastReadLineIndex = 0;
	}
	
}
