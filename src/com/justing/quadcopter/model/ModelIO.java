package com.justing.quadcopter.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jogamp.opengl.util.texture.TextureIO;
import com.justing.quadcopter.model.Model.MtllibResource;

public class ModelIO {

	/*
	 * NOTE: returns model from .obj file. Requirements:
	 * Info order: 
	 * First one, mtllib, without it, no work!
	 * 'v', 'vn', and 'vt' at the beginning 
	 * Then must be, 'usemtl'
	 * And only after that, all the 'f'
	 * 
	 * If multipe textures on model, other 'usemtl' should be between 'f' space
	 * 
	 * i.e.:
	 * 
	 * mtllib
	 * v
	 * vn
	 * vt
	 * usemtl
	 * f
	 * usemtl
	 * f....
	 */
	
	public final static Model newModel(String fileName) throws IOException{
		Model model = new Model();
		
		String line;
		String[] words;
		int[] w1, w2, w3;
		
		List<Vertice> listVertice = new ArrayList<>();
		List<VerticeTexture> listTexture = new ArrayList<>();
		List<Vertice> listNormale = new ArrayList<>();
		
		Vertice[] arrVertice = null;
		VerticeTexture[] arrTexture = null;
		Vertice[] arrNormale = null;
		
		List<Model.Face> faces = new ArrayList<>();
		List<MtllibResource> mtl = null;
		
		boolean isFacesStarted = false;
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
		
		// Reads all file
		while ((line = bufferedReader.readLine()) != null) {
			words = line.split(" ");
			if (words == null || words.length == 0) words = new String[]{"0", "0", "0", "0"};
			
			// Reads resources
			if (line.startsWith("mtllib ")){
				mtl = getMtlResources(model, words[1]);
			
			// Reads all vertices
		    } else if (line.startsWith("v ")){
				listVertice.add(new Vertice(words[1 % words.length], words[2 % words.length], words[3 % words.length]));
			} else if (line.startsWith("vn ")){
				listNormale.add(new Vertice(words[1 % words.length], words[2 % words.length], words[3 % words.length]));
			} else if (line.startsWith("vt ")){
				listTexture.add(new VerticeTexture(words[1 % words.length], words[2 % words.length]));
			
			// Change resource
			} else if (line.startsWith("usemtl ")){
				for (MtllibResource m : mtl){
					// if this is our mtl, save it's starting position acordint to faces added.
					if (m.label.equalsIgnoreCase(words[1])) m.start = faces.size();
				}
				
			// Reads all faces
			} else if (line.startsWith("f ")){
				// First time on face
				if (!isFacesStarted){
					isFacesStarted = true;
					
					// Converts everything to array from list for faster data access
					arrVertice = listVertice.toArray(new Vertice[listVertice.size()]);
					arrTexture = listTexture.toArray(new VerticeTexture[listTexture.size()]);
					arrNormale = listNormale.toArray(new Vertice[listNormale.size()]);
					
					// if no data for normals or textVertex
					if (arrNormale.length == 0) arrNormale = new Vertice[]{new Vertice(0, 0, 0)};
					if (arrTexture.length == 0) arrTexture = new VerticeTexture[]{new VerticeTexture(0, 0)};
				}
				
				w1 = strArrayToIntArray(words[1].split("/"));
				w2 = strArrayToIntArray(words[2].split("/"));
				w3 = strArrayToIntArray(words[3].split("/"));

				faces.add(model.new Face
							(new Vertice[]{arrVertice[w1[0] - 1], arrVertice[w2[0] - 1], arrVertice[w3[0] - 1]},
							new VerticeTexture[]{arrTexture[w1[1 % w1.length] - 1], arrTexture[w2[1 % w2.length] - 1], arrTexture[w3[1 % w3.length] - 1]},
							new Vertice[]{arrNormale[w1[2 % w1.length] - 1], arrNormale[w2[2 % w2.length] - 1], arrNormale[w3[2 % w3.length] - 1]})
						 );
			}
		}
			
		bufferedReader.close();
		
		
		// add everything to map
		Map<Integer, MtllibResource> mtlmap = new HashMap<>();
		for (MtllibResource m : mtl){
			mtlmap.put(m.start, m);
		}
		
		model.configureModel(faces, mtlmap);
		return model;
	}
	
	/*
	 * NOTE: after 'map_Kd' file names must not have spaces
	 */	
	private static List<MtllibResource> getMtlResources(Model model, String fileName) throws IOException {

		String line;
		String[] words;
		List<MtllibResource> mtl = new ArrayList<>();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader("res/obj/" + fileName));
		while ((line = bufferedReader.readLine()) != null) {
			words = line.split(" ");
			
			if (line.startsWith("newmtl ")){ // ads new object to list
				mtl.add(model.new MtllibResource(words[1]));
			}
			else if (line.startsWith("map_Kd ")){ // updates last added object texture
				mtl.get(mtl.size() - 1).texture = TextureIO.newTexture(new File("res/tex/" + words[1]), false);
			}
		}
		
		bufferedReader.close();
		
		return mtl;
	}

	private final static int[] strArrayToIntArray(String[] str){
		if (str == null || str.length == 0) str = new String[]{"0", "0", "0"};
		int[] i = new int[3];
		
		try {
			i[0] = Integer.parseInt(str[0]);
		} catch (NumberFormatException e) {
			i[0] = 1;
		}
		if (str.length > 1){
			try {
				i[1] = Integer.parseInt(str[1]);
			} catch (NumberFormatException e) {
				i[1] = 1;
			}
		} else i[1] = 1;
		
		if (str.length > 2){
			try {
				i[2] = Integer.parseInt(str[2]);
			} catch (NumberFormatException e) {
				i[2] = 1;
			}
		} else i[2] = 1;
		
		return i;
	}
}
