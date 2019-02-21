package com.yonyou.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class TreeUtil {
	
	/**
	 * 生成机构树
	* @param mylist
	* @return
	 */
	public static String getOrgTree(List<Map<String, Object>> mylist) {
		List<Map<String, Object>> jsonList = new ArrayList();
		if (!mylist.isEmpty()) {
			for (int i = 0; i < mylist.size(); i++) {
				Map<String, Object> jsoMap = new HashMap();
				Map searchParams = mylist.get(i);
				jsoMap.put("id", searchParams.get("TOTAL_CODE"));
				jsoMap.put("text", searchParams.get("NAME"));
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("totalCode", searchParams.get("TOTAL_CODE"));
				data.put("name", searchParams.get("NAME"));
				data.put("id", searchParams.get("ID"));
				data.put("url", searchParams.get("DATA_VALUE"));
				jsoMap.put("data", data);
				if (searchParams.get("PARENT_TOTAL_CODE") == null || searchParams.get("PARENT_TOTAL_CODE").equals("")) {
					jsoMap.put("parent", "#");
				} else {
					jsoMap.put("parent", searchParams.get("PARENT_TOTAL_CODE"));
				}
				jsonList.add(jsoMap);
			}

			return JsonUtils.object2json(jsonList);
		}else {
			return null;
		}
	}
	
	
	/**
	 * 生成机构树，并对部分节点进行选取标记
	 * 
	 * @param mylist
	 * @param nodeIds 需被标记为被选中的节点数组
	 * @return
	 */
	public static String getMarkedOrgTree(List<Map<String, Object>> mylist, String[] nodeIds) {
		List<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
		if (!mylist.isEmpty()) {
			for (int i = 0; i < mylist.size(); i++) {
				Map<String, Object> jsoMap = new HashMap<String, Object>();
				Map searchParams = mylist.get(i);
				jsoMap.put("id", searchParams.get("TOTAL_CODE"));
				jsoMap.put("text", searchParams.get("NAME"));
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("totalCode", searchParams.get("TOTAL_CODE"));
				data.put("name", searchParams.get("NAME"));
				data.put("id", searchParams.get("ID"));
				jsoMap.put("data", data);
				if (searchParams.get("PARENT_TOTAL_CODE") == null || searchParams.get("PARENT_TOTAL_CODE").equals("")) {
					jsoMap.put("parent", "#");
				} else {
					jsoMap.put("parent", searchParams.get("PARENT_TOTAL_CODE"));

				}
				// 对已授权资源进行选取
				Map<String, Object> state = new HashMap<String, Object>();
				String totalCode = searchParams.get("TOTAL_CODE").toString();
				List<String> ListString = Arrays.asList(nodeIds);
				Boolean selected = isSelected(mylist, totalCode, ListString);
				//设置状态
				if(selected){
					state.put("selected", true);
					jsoMap.put("state", state);
				}else{
					jsoMap.put("state", state);
				}
				/*if (Arrays.asList(nodeIds).contains(searchParams.get("TOTAL_CODE").toString())) {
					Map<String, Object> state = new HashMap<String, Object>();
					state.put("selected", true);
					jsoMap.put("state", state);
				}*/
				jsonList.add(jsoMap);
			}

			return JsonUtils.object2json(jsonList);
		}
		return null;
	}
	
	/**
	 * 判断是否可以被标记为选择状态
	* @param listMap
	* @param id
	* @param asList
	* @return
	 */
	public static Boolean isSelected(List<Map<String, Object>> listMap,String id,List<String> listString){
		List<String> totalCodeListByParentTotalCode = getTotalCodeListByParentTotalCode(listMap, id);
		boolean isMatched = true;
		for (String one : totalCodeListByParentTotalCode) {
			if(!listString.contains(one)){
				isMatched = false;
				break;
			}else{
				List<String> totalCodeListByTotalCode = getTotalCodeListByTotalCode(listMap, one);
				for (String two : totalCodeListByTotalCode) {
					if(!listString.contains(two)){
						isMatched = false;
						break;
					}
				}
			}
		}
		return isMatched;
	}
	
	/**
	 * 查询是否存在子级
	* @param listMap 全部
	* @param id	TOTAL_CODE
	* @return
	 */
	public static List<String> getTotalCodeListByParentTotalCode(List<Map<String, Object>> listMap,String id){
		List<String> stringList = Lists.newArrayList();
		for(int i=0;i<listMap.size();i++){
			Map<String, Object> searchParams = listMap.get(i);
			String parentTotalCode = searchParams.get("PARENT_TOTAL_CODE")!=null?searchParams.get("PARENT_TOTAL_CODE").toString():null;
			if(parentTotalCode.equals(id)){
				stringList.add(searchParams.get("TOTAL_CODE")!=null?searchParams.get("TOTAL_CODE").toString():null);
			}
		}
		if(stringList.size()==0){
			stringList.add(id);
		}
		return stringList;
	}
	
	/**
	 * 查询TOTAL_CODE包含的菜单
	* @param listMap
	* @param id TOTAL_CODE
	* @return
	 */
	public static List<String> getTotalCodeListByTotalCode(List<Map<String, Object>> listMap,String id){
		List<String> stringList = Lists.newArrayList();
		for(int i=0;i<listMap.size();i++){
			Map<String, Object> searchParams = listMap.get(i);
			String parentTotalCode = searchParams.get("TOTAL_CODE")!=null?searchParams.get("TOTAL_CODE").toString():null;
			if(parentTotalCode.contains(id)){
				stringList.add(searchParams.get("TOTAL_CODE")!=null?searchParams.get("TOTAL_CODE").toString():null);
			}
		}
		if(stringList.size()==0){
			stringList.add(id);
		}
		return stringList;
	}
	
}
