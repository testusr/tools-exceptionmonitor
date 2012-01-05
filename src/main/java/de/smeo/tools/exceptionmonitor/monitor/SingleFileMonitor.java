package de.smeo.tools.exceptionmonitor.monitor;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionParser;
import de.smeo.tools.exceptionmonitor.reporting.MonitoredFile;

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
	private MonitoredFile monitoredFile;
	private FileChunkReader fileChunkReader;
	
	public SingleFileMonitor(MonitoredFile monitoredFile) {
		this.monitoredFile = monitoredFile;
		fileChunkReader = new FileChunkReader();
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

	public synchronized void parseNewFileEntriesIfNecessary() {
		if ((lastFileCheckTime < 0) || (getFileCheckInterval() < 0) || (lastFileCheckTime + getFileCheckInterval()) <= getCurrTime()){
			parseNewFileEntriesAndReturnExceptions();
		}
	}
	
	private long getFileCheckInterval() {
		return monitoredFile.getCheckInterval();
	}

	public long getLastFileCheckTime() {
		return this.lastFileCheckTime;
	}
	
	public MonitoredFile getMonitoredFile() {
		return monitoredFile;
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
		private File fileToRead;

		private long lastReadFilePosition = 0;
		private long lastFileSize;

		private String lastLineOfLastChunk;
		
		public FileChunkReader() {
			fileToRead = new File(getMonitoredFile().getFilename());
		}

		/**
		 * getun the next chunk of full filens from a file (eding with a \n)
		 * @return
		 */
		public String getNextChunk() {
			if (fileToRead.exists()  && getMonitoredFile().isMonitored()){
				try {
					RandomAccessFile randomAccessFile = new RandomAccessFile(fileToRead, "r");
					resetFilePositionIfFileGotSmaller();
					
					byte[] destinationArray = new byte[MAX_CHUNK_SIZE];
					randomAccessFile.seek(lastReadFilePosition);
					int readBytes = randomAccessFile.read(destinationArray, 0, MAX_CHUNK_SIZE);
					randomAccessFile.close();

					StringBuffer newChunk = new StringBuffer();
					if (lastLineOfLastChunk != null){
						newChunk.append(lastLineOfLastChunk);
						lastLineOfLastChunk = null;
					}
					
					if (readBytes != -1){
						lastReadFilePosition = lastReadFilePosition+readBytes;
						
						String stringFromReadArray = createStringFromArray(destinationArray, readBytes);
						lastLineOfLastChunk = addStringWithoutLastLine(newChunk, stringFromReadArray);
						
					}

					if (newChunk.length() > 0){
						return newChunk.toString();
					}

				}  catch (Exception e) {
					e.printStackTrace();
					System.err.println("problem while reading file: " + fileToRead.getAbsoluteFile() + " will stop to read");
					getMonitoredFile().setMonitored(false);
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
			if (fileToRead.length() < lastFileSize){
				lastReadFilePosition = 0;
			}
		}

	}
	
}
