package main;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReadFileWithFixedSizeBuffer
{
    private static void saveToFile(String fn, StringBuilder sb,  long pos) throws IOException {

        FileChannel rwChannel = new RandomAccessFile(fn, "rw").getChannel();
        ByteBuffer wrBuf;
        wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, pos, sb.toString().getBytes().length);
        wrBuf.put(sb.toString().getBytes());
        rwChannel.close();
    }

    public static void main(String[] args) throws IOException
    {
        char dot = '.';
        char spacja = ' ';
        char ch;
        long pos = 0;
        String inFile = "C:\\Users\\John\\Downloads\\sample_data\\large.in";
        String outFile = "C:\\Users\\John\\Downloads\\sample_data\\big-file.xml" ;

        StringBuilder sb = new StringBuilder();

        RandomAccessFile aFile = new RandomAccessFile(inFile, "r");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1000000);

        while(inChannel.read(buffer) > 0)
        {
            buffer.flip();
            for (int i = 0; i < buffer.limit(); i++)
            {

                ch = (char) buffer.get();
                // todo:  xml format
                if (ch == spacja) {
                    sb.append("<word>");
                } else {
                    if (ch == dot) {
                        sb.append(System.lineSeparator());
                        sb.append("<sentence>");
                    } else {
                        sb.append(ch);
                    }
                }
                //
            }
            saveToFile(outFile,sb,pos);
            pos += sb.length();
            sb.setLength(0);
            buffer.clear();
        }
        inChannel.close();
        aFile.close();
        System.out.println("File saved!");
    }
}