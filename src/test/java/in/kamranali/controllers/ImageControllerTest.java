package in.kamranali.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import in.kamranali.commands.RecipeCommand;
import in.kamranali.services.ImageService;
import in.kamranali.services.RecipeService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
        		.setControllerAdvice(new ControllerExceptionHandler())
        		.build();
    }

    @Test
    public void getImageForm() throws Exception {
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        //when
        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandById(anyLong());

    }

    @Test
    public void handleImagePost() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).saveImageFile(anyLong(), any());
    }

    @Test
	public void testRenderImageFromDB() throws Exception {
		
		RecipeCommand command = new RecipeCommand();
		command.setId(1L);
		String image = "My fake Image";
		
		Byte[] imageBytes = new Byte[image.getBytes().length];
		int i = 0;
		
		for (Byte b : image.getBytes()) {
			imageBytes[i++] = b;
		}
		command.setImage(imageBytes);
		
		when(recipeService.findCommandById(Mockito.anyLong())).thenReturn(command);
		
		MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
		.andExpect(status().isOk())
		.andReturn().getResponse();
		
		assertEquals(image.getBytes().length, response.getContentAsByteArray().length);
	}
    
    @Test
	public void testRenderImageNumberFormatException() throws Exception {
		
		mockMvc.perform(get("/recipe/asdf/recipeimage"))
		.andExpect(status().isBadRequest())
		.andExpect(view().name("400error"));
		
	}
}
