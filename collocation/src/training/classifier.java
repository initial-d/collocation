package training;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.fudan.data.reader.SimpleFileReader;
import edu.fudan.ml.classifier.linear.Linear;
import edu.fudan.ml.classifier.linear.OnlineTrainer;
import edu.fudan.ml.classifier.linear.inf.Inferencer;
import edu.fudan.ml.classifier.linear.inf.LinearMax;
import edu.fudan.ml.classifier.linear.update.LinearMaxPAUpdate;
import edu.fudan.ml.feature.Generator;
import edu.fudan.ml.feature.SFGenerator;
import edu.fudan.ml.loss.ZeroOneLoss;
import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.nlp.pipe.StringArray2IndexArray;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.pipe.SeriesPipes;
import edu.fudan.nlp.pipe.Target2Label;

/**
 * 
 * @author duyimin
 * 
 */
public class classifier {
	static InstanceSet train;
	static InstanceSet test;
	static AlphabetFactory factory = AlphabetFactory.buildFactory();
	static LabelAlphabet al = factory.DefaultLabelAlphabet();
	static FeatureAlphabet af = factory.DefaultFeatureAlphabet();
	static String path = null;

	static List<String> getFiles(String filePath) {
		List<String> ans = new ArrayList<String>();
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			ans.add(file.getAbsolutePath());
		}
		return ans;
	}

	private static void train() throws Exception {
		long start = System.currentTimeMillis();

		Pipe lpipe = new Target2Label(al);
		Pipe fpipe = new StringArray2IndexArray(factory, false);

		Pipe pipe = new SeriesPipes(new Pipe[] { lpipe, fpipe });

		String ans = "D:\\workspace\\collocation\\obj\\";
		List<String> files = getFiles("E:\\STUDY\\JAVA\\collocation\\data\\");
		for (int i = 0; i < files.size(); i++) {
			String txt = files.get(i).substring(
					files.get(i).lastIndexOf('\\') + 1);
			path = files.get(i);
			train = new InstanceSet(pipe, factory);
			SimpleFileReader reader = new SimpleFileReader(path, true);
			train.loadThruStagePipes(reader);
			al.setStopIncrement(true);

			test = new InstanceSet(pipe, factory);
			reader = new SimpleFileReader(path, true);
			test.loadThruStagePipes(reader);

			System.out.println("Train Number: " + train.size());
			System.out.println("Test Number: " + test.size());
			System.out.println("Class Number: " + al.size());

			float c = 1.0f;
			int round = 20;

			Generator featureGen = new SFGenerator();
			ZeroOneLoss loss = new ZeroOneLoss();
			LinearMaxPAUpdate update = new LinearMaxPAUpdate(loss);

			Inferencer msolver = new LinearMax(featureGen, al.size());
			OnlineTrainer trainer = new OnlineTrainer(msolver, update, loss,
					af.size(), round, c);
			System.out.println(af + " " + af.keysize());

			Linear classify = trainer.train(train, test);

			classify.saveTo(ans + txt + ".m.gz");

			long end = System.currentTimeMillis();
			System.out.println("Total Time: " + (end - start));
		}
	}

	public static void main(String[] args) throws Exception {
		train();
	}
}
