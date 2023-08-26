package cn.liguohao.demo.jlibtorrent;

import com.frostwire.jlibtorrent.BDecodeNode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ReadBEncoded {

    public static void main(String[] args) throws Throwable {
        args = new String[] {
            "/Users/aldenml/Downloads/303dde355f99c9b903efaeba57e23194a7a6713f.resume"};

        byte[] data = Files.readAllBytes(Path.of(args[0]));

        BDecodeNode n = BDecodeNode.bdecode(data);

        System.out.println(n);
    }
}
