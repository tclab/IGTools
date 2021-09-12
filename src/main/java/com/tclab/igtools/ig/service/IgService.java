package com.tclab.igtools.ig.service;

import com.tclab.igtools.ig.dto.IgPostDto;
import com.tclab.igtools.ig.dto.IgResponseDto;

public interface IgService {


  IgResponseDto postImage(IgPostDto postDto) throws Exception;

}
