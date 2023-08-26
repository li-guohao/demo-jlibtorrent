package cn.liguohao.demo.jlibtorrent;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DownloadTorrent {

    // static {
    //     String ddlPathStr = System.getProperty("user.dir")
    //         + File.separatorChar + "libs" + File.separatorChar + "jlibtorrent-1.2.19.0.dll";
    //     System.load(ddlPathStr);
    // }

    public static void main(String[] args) throws Throwable {
        String ddlPathStr = System.getProperty("user.dir")
            + File.separatorChar + "libs" + File.separatorChar + "jlibtorrent-1.2.19.0.dll";
        System.load(ddlPathStr);

        // comment this line for a real application
        // args = new String[]{"C:\\Users\\li-guohao\\Documents\\demos\\aea716b151b1e7e25a13a4c939f0b8f4cbf3a6a3.torrent"};

        File torrentFile = new File("C:\\Users\\li-guohao\\Documents\\demos\\aea716b151b1e7e25a13a4c939f0b8f4cbf3a6a3.torrent");

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        final SessionManager s = new SessionManager();

        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();

                switch (type) {
                    case ADD_TORRENT:
                        System.out.println("Torrent added");
                        ((AddTorrentAlert) alert).handle().resume();
                        break;
                    case BLOCK_FINISHED:
                        BlockFinishedAlert a = (BlockFinishedAlert) alert;
                        int p = (int) (a.handle().status().progress() * 100);
                        System.out.println("Progress: " + p + " for torrent name: " + a.torrentName());
                        System.out.println(s.stats().totalDownload());
                        break;
                    case TORRENT_FINISHED:
                        System.out.println("Torrent finished");
                        signal.countDown();
                        break;
                }
            }
        });

        s.start();

        TorrentInfo ti = new TorrentInfo(torrentFile);
        s.download(ti, torrentFile.getParentFile());

        signal.await();

        s.stop();
    }
}
