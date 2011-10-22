package de.smeo.tools.exceptionmonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.List;

import de.smeo.tools.exceptionmonitor.exceptionparser.EqualCauseExceptionChainContainer;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionParser;

public class SingleFileMonitor {
	private long lastFileCheckTime = -1;
	private ExceptionParser exceptionParser = new ExceptionParser();
	private MonitoredFile monitoredFile;
	private SingleFileExceptionReport allDayExceptionReport;
	private SingleFileExceptionReport exceptionReportSinceLastUpdate;
	private FileChunkReader fileChunkReader;
	
	public SingleFileMonitor(MonitoredFile monitoredFile) {
		this.monitoredFile = monitoredFile;
		fileChunkReader = new FileChunkReader();

		allDayExceptionReport = new SingleFileExceptionReport(monitoredFile);
		exceptionReportSinceLastUpdate = new SingleFileExceptionReport(monitoredFile);

	}

	public synchronized List<ExceptionCausedByChain> checkFile() 
	{
		lastFileCheckTime = getCurrTime();
		exceptionParser.clean();
		String nextLogFileChunk = null;
		while ( (nextLogFileChunk = getNextLogFileChunk()) != null){
			exceptionParser.parse(nextLogFileChunk);
		}
		List<EqualCauseExceptionChainContainer> exceptionChains = exceptionParser.getExceptionGroupedByRootCause();
		
		allDayExceptionReport.addExceptions(exceptionChains);
		exceptionReportSinceLastUpdate.addExceptions(exceptionChains);
		
		return exceptionParser.getExceptionChains();
	}
	
	protected String getNextLogFileChunk() {
		return fileChunkReader.getNextChunk();
	}

	public synchronized void checkFileIfNecessary() {
		if ((lastFileCheckTime < 0) || (getFileCheckInterval() < 0) || (lastFileCheckTime + getFileCheckInterval()) <= getCurrTime()){
			checkFile();
		}
	}
	
	public SingleFileExceptionReport getExceptionsSinceLastUpdateAndReset() {
		SingleFileExceptionReport exceptionsSinceLastUpdate = this.exceptionReportSinceLastUpdate;
		this.exceptionReportSinceLastUpdate = new SingleFileExceptionReport(getMonitoredFile());
		return exceptionsSinceLastUpdate;
	}
	
	public SingleFileExceptionReport getAllDayExceptionReport() {
		return this.allDayExceptionReport;
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


	public class FileChunkReader {
		private int MAX_CHUNK_SIZE = 1024 * 100;
		private File fileToRead;

		private long lastReadFilePosition = 0;
		private long lastFileSize;

		private String lastLineOfLastChunk;
		
		public FileChunkReader() {
			fileToRead = new File(getMonitoredFile().getFilename());
		}

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
