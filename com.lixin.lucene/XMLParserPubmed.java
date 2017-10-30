
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;




/**
 * Created by lx201 on 2017/10/25.
 * 得到每个文章的PMID、标题、摘要、mesh词和关键词
 */
public class XMLParserPubmed {


    public static void getXf(String filename) {
        int T = 0; //对标题的数目进行计数
        int A = 0; //对摘要的数目进行计数

        //创建saxReader对象
        SAXReader reader = new SAXReader();
        //reader.setValidation(false);
        reader.setEntityResolver(new IgnoreDTDEntityResolver()); // ignore dtd

        //通过read方法读取一个文件，转化为Document对象
        try {
            Document document = reader.read(new File(filename));
            //获取根元素节点
            Element node = document.getRootElement();
            //取得子节点
            for (Iterator pp = node.elementIterator(); pp.hasNext(); ) {
                Element p = (Element) pp.next();
                //Element p = p1.element("PubmedArticle");

                Element MedlineCitation = p.element("MedlineCitation");

                //获取每篇文章的PMID
                Element PMID = MedlineCitation.element("PMID");
                String pmid = PMID.getText();
                //System.out.println(pmid);


                //获取文章标题
                Element Article = MedlineCitation.element("Article");
                Element ArticleTitle = Article.element("ArticleTitle");
                String title = ArticleTitle.getText();
                //System.out.println(title);


                //获取文章摘要
                String abs = "";
                if (Article.element("Abstract") != null) {
                    Element Abstract = Article.element("Abstract");
                    Element AbstractText = Abstract.element("AbstractText");
                    abs = AbstractText.getText();
                    //System.out.println(abs);
                } else {
                    abs = "";
                }


                //获取每篇文章的mesh词
                String mesh = "";
                if (MedlineCitation.element("MeshHeadingList") != null) {
                    Element MeshHeadingList = MedlineCitation.element("MeshHeadingList");
                    List MeshHeading = MeshHeadingList.elements("MeshHeading");
                    for (Iterator it = MeshHeading.iterator(); it.hasNext(); ) {
                        Element meshNode = (Element) it.next();
                        Element Descriptor = meshNode.element("DescriptorName");
                        mesh = mesh + Descriptor.getText() + ";";
                    }
                    //System.out.print(mesh);
                } else {
                    mesh = " ";
                }

                //获取每篇文章的关键词
                String kys = "";
                if (MedlineCitation.element("KeywordList") != null) {
                    Element KeywordList = MedlineCitation.element("KeywordList");
                    List Keywords = KeywordList.elements("Keyword");
                    for (Iterator it = Keywords.iterator(); it.hasNext(); ) {
                        Element keynode = (Element) it.next();
                        kys = kys + keynode.getText() + ";";

                    }

                } else {
                    kys = " ";
                }

                //得到标题、摘要、关键词和mesh次一起的一个文本
                String str = title + abs + kys + mesh;
                //System.out.println(str);

                //每一篇文章写一行
                File ff = new File("C://Users//lx201//Desktop//resultrenke.txt");
                try {
                    FileUtils.writeStringToFile(ff, pmid + '\t' + str + '\n', true);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        File f = new File("G://pubmedAllFiles");
        String[] filename = f.list();
        for (String name : filename) {
            String path1 = "G://pubmedAllFiles//";
            System.out.println(path1 + name);
            getXf(path1 + name);
        }

    }
}

//    public static void main(String[] args) {
//        getXf("C://Users//lx201//Desktop//pubmed_result.xml");
//
//    }
//}
