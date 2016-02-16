package edu.fudan.ml.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.fudan.data.reader.Reader;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.pipe.SeriesPipes;

/**
 * 鏍锋湰闆嗗悎
 * 
 * @author xpqiu
 * 
 */
public class InstanceSet extends ArrayList<Instance> {

    private static final long serialVersionUID = 3449458306217680806L;
    /**
     * 鏈牱鏈泦鍚堥粯璁ょ殑鏁版嵁绫诲瀷杞崲绠￠亾
     */
    private Pipe pipes = null;
    /**
     * 鏈牱鏈泦鍚堝搴旂殑鐗瑰緛鍜屾爣绛剧储寮曞瓧鍏哥鐞嗗櫒
     */
    private AlphabetFactory factory = null;
    
    
    public int numFeatures = 0;
    public String name = "";

    public InstanceSet(Pipe pipes) {
        this.pipes = pipes;
    }

    public InstanceSet(Pipe pipes, AlphabetFactory factory) {
        this.pipes = pipes;
        this.factory = factory;
    }

    public InstanceSet(AlphabetFactory factory) {
        this.factory = factory;
    }
    
    public InstanceSet() {
    }

    

    /**
     * 鍒嗗壊鏍锋湰闆嗭紝灏嗘牱鏈泦鍚堜腑鏍锋湰鏀鹃殢鏈烘斁鍦ㄤ袱涓泦鍚堬紝澶у皬鍒嗗埆涓篿/n,(n-i)/n
     * 
     * @param i 绗竴涓泦鍚堟瘮渚� 
     * @param n 闆嗗悎鏍锋湰鎬绘暟锛堢浉瀵逛簬i锛�
     * @return
     */
    public InstanceSet[] split(int i, int n) {
        return split((float) i/(float)n);
    }
    
    /**
     * 鍒嗗壊鏍锋湰闆嗭紝灏嗘牱鏈泦鍚堜腑鏍锋湰鏀鹃殢鏈烘斁鍦ㄤ袱涓泦鍚堬紝澶у皬鍒嗗埆涓篿/n,(n-i)/n
     * 
     * @param percent 鍒嗗壊姣斾緥 蹇呴』鍦�0,1涔嬮棿
     * @return
     */
    public InstanceSet[] split(float percent) {
        shuffle();
        int length = this.size();
        InstanceSet[] sets = new InstanceSet[2];
        sets[0] = new InstanceSet(pipes, factory);
        sets[1] = new InstanceSet(pipes, factory);
        int idx = (int) Math.round(percent*length);
        sets[0].addAll(subList(0, idx));
        if(idx+1<length)            
            sets[1].addAll(subList(idx+1, length));
        return sets;
    }

    public InstanceSet[] randomSplit(float percent) throws Exception {
        if (percent > 1 || percent < 0)
            throw new Exception("Percent should be in [0, 1]");
//        shuffle();
        InstanceSet[] sets = new InstanceSet[2];
        sets[0] = new InstanceSet(pipes, factory);
        sets[1] = new InstanceSet(pipes, factory);
        int[] flag = labelFlag();
        List<ArrayList<Integer>> list = listLabel(flag);
        flag = randomSet(flag, list, percent);
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] < 0) 
                sets[0].add(this.get(i));
            else
                sets[1].add(this.get(i));
        }
        return sets;
    }

    public int[] randomSet(int[] flag, List<ArrayList<Integer>> list, float percent) {
        Random r = new Random();
        for(ArrayList<Integer> alist : list) {
            int allsize = Math.round(alist.size() * percent);
            int count = 0;
            while (true) {
                int randomInt = r.nextInt(alist.size());
                int index = alist.get(randomInt);
                if (flag[index] >= 0) {
                    flag[index] = -1;
                    count++;
                    if (count >= allsize)
                        break;
                }
            }
        }
        return flag;
    }

    public List<ArrayList<Integer>> listLabel(int[] flag) {
        List<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
        int classsize = classSize().size();
        for (int i = 0; i < classsize; i++) {
            List<Integer> ll = new ArrayList<Integer>();
            list.add((ArrayList<Integer>)ll);
        }
        for (int i = 0; i < flag.length; i++) {
            int ele = flag[i];
            ArrayList<Integer> l = list.get(ele);
            l.add(i);
        }
        return list;
    }

    public int[] labelFlag() {
        int length = this.size();
        int[] flag = new int[length];
        Map<Object,Integer> map = classSize();
        for (int i = 0; i < length; i++) {
            Object target = this.get(i).getTarget();
            int label = map.get(target);
            flag[i] = label;
        }
        return flag;
    }

    public Map<Object, Integer> classSize() {
        Map<Object, Integer> map = new HashMap<Object, Integer>();
        int label = 0;
        for (Instance ins : this) {
            if (!map.containsKey(ins.getTarget())) {
                map.put(ins.getTarget(), label++);
            }
        }
        return map;
    }
    
    public InstanceSet subSet(int from,int end){
        InstanceSet set = new InstanceSet();
        set = new InstanceSet(pipes, factory);
        set.addAll(subList(from,end));
        return set;
    }

    /**
     * 鐢ㄦ湰鏍锋湰闆嗗悎榛樿鐨勨�滄暟鎹被鍨嬭浆鎹㈢閬撯�濋�氳繃鈥滄暟鎹鍙栧櫒鈥濇壒閲忓缓绔嬫牱鏈泦鍚�
     * @param reader 鏁版嵁璇诲彇鍣�
     * @throws Exception
     */
    public void loadThruPipes(Reader reader) throws Exception {

        // 閫氳繃杩唬鍔犲叆鏍锋湰
        while (reader.hasNext()) {
            Instance inst = reader.next();
            if (pipes != null)
                pipes.addThruPipe(inst);
            this.add(inst);
        }
    }

    /**
     * 鍒嗘楠ゆ壒閲忓鐞嗘暟鎹紝姣忎釜Pipe澶勭悊瀹屾墍鏈夋暟鎹啀杩涜涓嬩竴涓狿ipe
     * 
     * @param reader
     * @throws Exception
     */
    public void loadThruStagePipes_test(Reader reader) throws Exception {
        SeriesPipes p = (SeriesPipes) pipes;
        // 閫氳繃杩唬鍔犲叆鏍锋湰
        Pipe p1 = p.getPipe(0);
        Instance inst = reader.next();
        if(inst!=null){
        	if (p1 != null)
        		p1.addThruPipe(inst);
            this.add(inst);
        }
        for (int i = 1; i < p.size(); i++)
            p.getPipe(i).process(this);
    }

    
    public void loadThruStagePipes(Reader reader) throws Exception {
        SeriesPipes p = (SeriesPipes) pipes;
        // 通过迭代加入样本
        Pipe p1 = p.getPipe(0);
        while (reader.hasNext()) {
            Instance inst = reader.next();
            if(inst!=null){
                if (p1 != null)
                    p1.addThruPipe(inst);
                this.add(inst);
            };
        }
        for (int i = 1; i < p.size(); i++)
            p.getPipe(i).process(this);
    }
    
    
    
    public void shuffle() {
        Collections.shuffle(this);
    }

    public void shuffle(Random r) {
        Collections.shuffle(this, r);
    }

    public Pipe getPipes() {
        return pipes;
    }

    public Instance getInstance(int idx) {
        if (idx < 0 || idx > this.size())
            return null;
        return this.get(idx);
    }

    public AlphabetFactory getAlphabetFactory() {
        return factory;
    }

    public void addAll(InstanceSet subset) {
        this.addAll(subset);
    }

    public void setPipes(Pipe pipes) {
        this.pipes = pipes;
    }

    public void setAlphabetFactory(AlphabetFactory factory) {
        this.factory = factory;
    }

}
