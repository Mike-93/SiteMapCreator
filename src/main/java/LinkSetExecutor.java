import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class LinkSetExecutor extends RecursiveTask<String> {

    private String url;
    private static Set<String> allLinks = new HashSet<>();

    public LinkSetExecutor(String url) {
        this.url = url.trim();
    }

    public String getUrl() {
        return url;
    }

    @Override
    protected String compute() {
        StringBuffer stringBuffer = new StringBuffer(url + "\n");
        Set<LinkSetExecutor> subTask = new HashSet<>();

        getChildren(subTask);

        for (LinkSetExecutor link : subTask) {
            int slashCount = link.getUrl().length() - link.getUrl().replace("/", "").length();

            if (slashCount == 4) {
                stringBuffer.append("\t" + link.join());
            } else {
                if (slashCount == 5) {
                    stringBuffer.append("\t" + "\t" + link.join());
                } else {
                    if (slashCount == 6) {
                        stringBuffer.append("\t" + "\t" + "\t" + link.join());
                    } else {
                        stringBuffer.append(link.join());
                    }
                }
            }
        }

        return stringBuffer.toString();
    }

    private void getChildren(Set<LinkSetExecutor> subTask) {
        try {
            Thread.sleep(200);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("a");
            for (Element element : elements) {
                String attr = element.attr("abs:href");
                if (!attr.isEmpty() && attr.startsWith(url) && !allLinks.contains(attr) && !attr
                        .contains("#")) {
                    LinkSetExecutor linkSetExecutor = new LinkSetExecutor(attr);
                    linkSetExecutor.fork();
                    subTask.add(linkSetExecutor);
                    allLinks.add(attr);
                }
            }
        } catch (InterruptedException | IOException exception) {
        }
    }
}
