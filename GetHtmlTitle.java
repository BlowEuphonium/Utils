import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p>Title: software.GetHtmlTitle</p>
 * <p>Description: 依照指定url爬取网页以及获取网页title</p>
 * <p>Copyright:Copyright(c)2022BE-SOFT</p>
 * <p>Company:BE-SOFT</p>
 *
 * @author github?userid=BlowEuphonium 软件1902 柳清瀚 193415020129
 * @version 1.0
 * @Class GetHtmlTitle
 */
public class GetHtmlTitle {
    /**
     * 从指定url下载网页内容
     * @param address url
     * @param param 其它参数
     * @return 网页内容字符串
     */
    private static String getHtml(String address, String param) {
        StringBuilder result = new StringBuilder();
        String urlName = "";
        try {
            urlName = address + param;
            URL url = new URL(urlName);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line).append("\n");
            }
            in.close();
        } catch (Exception e) {
            System.out.println("与服务器链接发生异常错误 : " + e);
            System.out.println("链接地址是: " + urlName);
        }
        return result.toString();
    }

    /**
     * 保存网页内容到 ./crawler/ 下
     * @param path 程序完整路径(此处使用绝对路径)
     */
    public static void saveHtml(String path) {
        BufferedReader br = null;
        String extension = ".html";
        int name = 1;
        try {
            br = new BufferedReader(new InputStreamReader(
                    Files.newInputStream(Paths.get(path + "urls\\html_urls.txt"))));
            String url = null;
            while ((url = br.readLine()) != null) {
                String html = getHtml(url, "");
                String title = getTitle(html);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "crawler\\" + "第" + name++ + "个网页" + title + extension, true)));
                bw.write(html);
                bw.flush();
                bw.close();
                System.out.println("第" + (name-1) + "个网页保存成功!");
            }
            System.out.println("网页存到这里啦: " + path + "crawler\\");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从网页文件中获取网页标题
     * @param html 网页内容
     * @return 网页标题
     */
    private static String getTitle(String html) {
        int begin = html.indexOf("<title>");
        if(begin < 0){
            System.out.println("此网页无标题? 再检查一下吧!");
            return "";
        }
        int end = html.indexOf("</title>");
        if(end < begin){
            System.out.println("此网页格式有误! 再检查一下吧!");
            return "";
        }
        return html.substring(begin + "<title>".length(), end);
    }

    /**
     * 保存网页标题为指定格式至 ./result/result.txt
     * @param path 程序完整路径(此处使用绝对路径)
     */
    public static void saveTitle(String path){
        File[] files = new File(path + "crawler\\").listFiles();
        if(null == files){
            System.out.println("此文件夹内没有文件!");
            return ;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(path + "result/result.txt"))));

            for (File file: files) {
                String fileName = file.getName();
                if(!fileName.endsWith(".html")) continue;
                BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(path + "crawler/" + fileName))));
                StringBuilder html = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    html.append(line);
                }
                br.close();

                String title = getTitle(html.toString());

                bw.write(path + "crawler\\" + fileName + "#" + title + "\n");
                System.out.println("网页 : " + fileName + " 写入成功!");
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        File f = new File("");
        String path = f.getAbsolutePath() + "\\";
        // url输入路径为: ./urls/html.urls.txt
        // 结果输出路径为: ./result/result.txt

        saveHtml(path);
        saveTitle(path);
    }
}
